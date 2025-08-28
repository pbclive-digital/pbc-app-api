package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private val authService: AuthService? = null

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun createUser(user: User): ResponseEntity<BaseResponse<String>>? {
        userFromId(user.id)?.let {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.CONFLICT.toString()))
                ))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse(Status.SUCCESS,
                    datastoreRepositoryContract.createEntity(DatastoreConstant.USER_COLLECTION, user.id, user), null))
        }
    }

    fun updateUser(userId: String, newUser: User): ResponseEntity<BaseResponse<User>>? {
        val user = getUserById(userId)
        user?.let {
            datastoreRepositoryContract.updateEntity(DatastoreConstant.USER_COLLECTION, userId, newUser)
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS,
                newUser, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getUserById(userId: String): ResponseEntity<BaseResponse<User>>? {
        userFromId(userId)?.let {
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS, it, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getUserByEmail(email: String): ResponseEntity<BaseResponse<User>>? {
        userFromEmail(email)?.let {
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS, it, null))
        }?:run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun deleteUserFromId(userId: String): ResponseEntity<BaseResponse<String>>? {
        val updateTime = datastoreRepositoryContract.deleteEntity(DatastoreConstant.USER_COLLECTION, userId)
        updateTime?.let {
            authService?.deleteTokenFromUser(userId)
        }
        return ResponseEntity.ok(BaseResponse(Status.SUCCESS, updateTime, null))
    }

    private fun userFromId(userId: String): User? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.USER_COLLECTION, userId, User::class.java)?.let {
            return it
        }?: run {
            return null
        }
    }

    private fun userFromEmail(email: String): User? {
        var user: User? = null
        val responseList: List<User> = datastoreRepositoryContract.getEntityListFromProperty(DatastoreConstant.USER_COLLECTION,
            "email", email, User::class.java)
        if (responseList.isNotEmpty())
            user = responseList.get(0)

        return user
    }
}