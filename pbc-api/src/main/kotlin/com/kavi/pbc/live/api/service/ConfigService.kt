package com.kavi.pbc.live.api.service

import com.kavi.droid.survey.api.dto.BaseResponse
import com.kavi.droid.survey.api.dto.Error
import com.kavi.droid.survey.api.dto.Status
import com.kavi.pbc.live.api.AppProperties
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDataRepository
import com.kavi.pbc.live.data.model.config.Config
import com.kavi.pbc.live.data.repository.DataRepository
import com.kavi.pbc.live.data.repository.db.DBConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ConfigService {

    @Autowired
    lateinit var appProperties: AppProperties

    private var dataRepository: DataRepository = FirebaseDataRepository()

    fun getConfigByVersion(version: String): ResponseEntity<BaseResponse<Config>> {
        dataRepository.getEntityFromId(DBConstant.CONFIG_COLLECTION, version, Config::class.java)?.let {
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS, it, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString())
                )))
        }

        //return ResponseEntity.ok(BaseResponse(Status.SUCCESS, Config(), null))
    }

    fun createConfigByVersion(version: String, config: Config): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                dataRepository.createEntity(DBConstant.CONFIG_COLLECTION, version, config), null))
    }
}