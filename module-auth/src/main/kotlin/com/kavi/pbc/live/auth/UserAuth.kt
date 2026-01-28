package com.kavi.pbc.live.auth

import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.DataConstant
import com.kavi.pbc.live.data.model.auth.AuthToken
import com.kavi.pbc.live.data.model.auth.AuthTokenStatus
import com.kavi.pbc.live.data.model.user.User
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.data.util.DataUtil
import java.time.temporal.ChronoUnit

class UserAuth {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    fun userStatus(email: String, userId: String): String {
        var user: User? = null
        val properties = mapOf(
            "email" to email,
            "id" to userId
        )

        val responseList: List<User> = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.USER_COLLECTION,
            propertiesMap = properties,
            className = User::class.java)
        if (responseList.isNotEmpty())
            user = responseList[0]

        user?.let {
            return "REGISTERED"
        }?: run {
            return "UNREGISTERED"
        }
    }

    fun getUser(email: String, userId: String): User? {
        var user: User? = null
        val properties = HashMap<String, String>()
        properties["email"] = email
        properties["id"] = userId
        val responseList: List<User> = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.USER_COLLECTION,
            propertiesMap = properties,
            className = User::class.java)
        if (responseList.isNotEmpty())
            user = responseList[0]

        return user
    }

    fun requestToken(email :String, userId: String): AuthToken? {
        var authToken: AuthToken? = null
        val properties = HashMap<String, String>()
        properties["email"] = email
        properties["userId"] = userId
        val responseList: List<AuthToken> = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.TOKEN_COLLECTION,
            propertiesMap = properties,
            className = AuthToken::class.java)

        if (responseList.isNotEmpty())
            authToken = responseList[0]

        authToken?.let {
            // Update using token lastUsedAt entity.
            it.lastUsedAt = DataUtil.getCurrentTimestamp()
            datastoreRepositoryContract.updateEntity(DatastoreConstant.TOKEN_COLLECTION, it.id!!, it)

            return it
        }?: run {
            return null
        }
    }

    fun generateAuthToken(authToken: AuthToken): AuthToken {
        authToken.id = DataUtil.idGenerator("tkn")
        authToken.token = TokenGenerator().generateToken(authToken)
        authToken.lastUsedAt = DataUtil.getCurrentTimestamp()

        println(">>>>>>>>>>>> AuthToken : $authToken")

        datastoreRepositoryContract.createEntity(DatastoreConstant.TOKEN_COLLECTION, authToken.id!!, authToken)
        return authToken
    }

    fun validateAuthToken(token: String): AuthToken? {
        val properties = HashMap<String, String>()
        properties["token"] = token
        val responseList: List<AuthToken> = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.TOKEN_COLLECTION,
            propertiesMap = properties,
            className = AuthToken::class.java)

        return if (responseList.isNotEmpty()) {
            val authToken = responseList[0]
            when(authToken.status) {
                AuthTokenStatus.VALID -> authToken
                else -> null
            }
        } else {
            return null
        }
    }

    fun removeGivenToken(tokenId: String): String? {
        return datastoreRepositoryContract.deleteEntity(entityCollection = DatastoreConstant.TOKEN_COLLECTION, entityId = tokenId)
    }

    fun removeAllTokensForUser(userId: String) {
        val properties = HashMap<String, String>()
        properties["userId"] = userId

        val authTokenList: List<AuthToken> = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.TOKEN_COLLECTION,
            propertiesMap = properties,
            className = AuthToken::class.java)

        authTokenList.forEach {
            datastoreRepositoryContract.deleteEntity(
                entityCollection = DatastoreConstant.TOKEN_COLLECTION,
                entityId = it.id!!
            )
        }
    }

    fun removeOlderTokens(): List<String> {
        val deletedTokenOwnerList = mutableListOf<String>()
        val lessThanMap = HashMap<String, Long>()

        lessThanMap["lastUsedAt"] = DataUtil.getOlderTimestamp(DataConstant.AUTH_TOKEN_LIFETIME, ChronoUnit.DAYS)

        val responseList: List<AuthToken> = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.TOKEN_COLLECTION,
            lessThanMap = lessThanMap,
            className = AuthToken::class.java)

        responseList.forEach { token ->
            datastoreRepositoryContract.deleteEntity(DatastoreConstant.TOKEN_COLLECTION, token.id!!)
            deletedTokenOwnerList.add(token.email)
        }

        return deletedTokenOwnerList
    }
}