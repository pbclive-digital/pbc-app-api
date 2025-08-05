package com.kavi.pbc.live.api.service

import com.kavi.droid.survey.api.dto.BaseResponse
import com.kavi.droid.survey.api.dto.Error
import com.kavi.droid.survey.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDataRepository
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.data.repository.DataRepository
import com.kavi.pbc.live.data.repository.db.DBConstant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService {

    private var dataRepository: DataRepository = FirebaseDataRepository()

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
                    dataRepository.createEntity(DBConstant.USER_COLLECTION, user.id, user), null))
        }
    }

    fun updateUser(userId: String, newUser: User): ResponseEntity<BaseResponse<User>>? {
        val user = getUserById(userId)
        user?.let {
            dataRepository.updateEntity(DBConstant.USER_COLLECTION, userId, newUser)
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

    private fun userFromId(userId: String): User? {
        dataRepository.getEntityFromId(DBConstant.USER_COLLECTION, userId, User::class.java)?.let {
            return it
        }?: run {
            return null
        }
    }

    private fun userFromEmail(email: String): User? {
        var user: User? = null
        val responseList: List<User> = dataRepository.getEntityListFromProperty(DBConstant.USER_COLLECTION,
            "email", email, User::class.java)
        if (responseList.isNotEmpty())
            user = responseList.get(0)

        return user
    }
}