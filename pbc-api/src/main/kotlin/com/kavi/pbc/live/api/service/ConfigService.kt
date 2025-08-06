package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.api.AppProperties
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.DatastoreRepositoryContract
import com.kavi.pbc.live.data.model.config.AppVersionStatus
import com.kavi.pbc.live.data.model.config.Config
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDatastoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ConfigService {

    @Autowired
    lateinit var appProperties: AppProperties

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun getConfigByVersion(version: String): ResponseEntity<BaseResponse<Config>> {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.CONFIG_COLLECTION, version, Config::class.java)?.let {
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS, it, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString())
                )))
        }
    }

    fun createConfigByVersion(version: String, config: Config): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.CONFIG_COLLECTION, version, config), null))
    }

    fun isApiSupportForAppVersion(deviceOS: String, appVersion: String): ResponseEntity<BaseResponse<AppVersionStatus>>? {
        val androidSupportVersion = appProperties.androidSupportVersion
        val iOSSupportVersion = appProperties.iOSSupportVersion
        lateinit var baseResponse: BaseResponse<AppVersionStatus>
        try {
            when (deviceOS) {
                "android" -> {
                    baseResponse = BaseResponse(
                        Status.SUCCESS, AppVersionStatus(
                            support = versionCheck(appVersion = appVersion, configVersion = androidSupportVersion),
                            supportingVersion = androidSupportVersion
                        ), null
                    )

                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(baseResponse)
                }

                "ios" -> {
                    baseResponse = BaseResponse(
                        Status.SUCCESS, AppVersionStatus(
                            support = versionCheck(appVersion = appVersion, configVersion = iOSSupportVersion),
                            supportingVersion = iOSSupportVersion
                        ), null
                    )

                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(baseResponse)
                }

                else -> {
                    baseResponse = BaseResponse(
                        Status.ERROR, null, listOf(
                            Error("Given Device OS is not supports")
                        )
                    )

                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(baseResponse)
                }
            }
        } catch (ex: NumberFormatException) {
            baseResponse = BaseResponse(
                Status.ERROR, null, listOf(
                    Error("Given AppVersion is not in correct format: ${ex.message}")
                )
            )

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(baseResponse)
        }
    }

    @Throws(NumberFormatException::class)
    private fun versionCheck(appVersion: String, configVersion: String): Boolean {
        val appVersionSegments = appVersion.split(".")
        val configVersionSegments = configVersion.split(".")

        try {
            if (appVersionSegments[0].toInt() > configVersionSegments[0].toInt()) {
                return true
            } else if (appVersionSegments[0].toInt() == configVersionSegments[0].toInt()) {
                return if (appVersionSegments[1].toInt() > configVersionSegments[1].toInt()) {
                    true
                } else if (appVersionSegments[1].toInt() == configVersionSegments[1].toInt()) {
                    if (appVersionSegments[2].toInt() > configVersionSegments[2].toInt()) {
                        true
                    } else if (appVersionSegments[2].toInt() == configVersionSegments[2].toInt()) {
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } else {
                return false
            }
        } catch (ex: NumberFormatException) {
            throw ex
        }
    }
}