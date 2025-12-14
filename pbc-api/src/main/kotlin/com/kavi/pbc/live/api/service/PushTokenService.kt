package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.notification.PushTokenData
import com.kavi.pbc.live.data.model.notification.UserPushToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PushTokenService {

    @Autowired
    lateinit var logger: AppLogger
    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun updatePushNotificationToken(userId: String, pushTokenData: PushTokenData): ResponseEntity<BaseResponse<String>>? {
        getUserPushTokenFromId(userId)?.let {
            if (it.pushTokenList.contains(pushTokenData.pushToken)) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(BaseResponse(Status.ERROR, null, listOf(
                        Error("Provided PushNotification Token is already available for given User."))
                    ))
            } else {
                it.pushTokenList.add(pushTokenData.pushToken)
                return ResponseEntity.ok(BaseResponse(Status.SUCCESS,
                    datastoreRepositoryContract.updateEntity(
                        DatastoreConstant.USER_PUSH_TOKEN_COLLECTION,
                        userId, it),
                    null)
                )
            }
        }?: run {
            val pushToken = UserPushToken(userId, pushTokenData.email, mutableListOf(pushTokenData.pushToken))

            return ResponseEntity.ok(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(
                    DatastoreConstant.USER_PUSH_TOKEN_COLLECTION,
                    userId, pushToken),
                null)
            )
        }
    }

    fun getAllPushTokens(): List<String> {
        val allAvailableTokenList = mutableListOf<String>()
        datastoreRepositoryContract.getAllInEntity(DatastoreConstant.USER_PUSH_TOKEN_COLLECTION,
            UserPushToken::class.java).forEach { userPushToken ->
                userPushToken.pushTokenList.forEach { token ->
                    allAvailableTokenList.add(token)
                }
        }

        return allAvailableTokenList
    }

    private fun getUserPushTokenFromId(userId: String): UserPushToken? {
        return datastoreRepositoryContract.getEntityFromId(
            DatastoreConstant.USER_PUSH_TOKEN_COLLECTION,
            userId, UserPushToken::class.java)
    }
}