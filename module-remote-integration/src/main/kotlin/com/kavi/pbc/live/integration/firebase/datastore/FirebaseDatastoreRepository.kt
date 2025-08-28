package com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QuerySnapshot
import com.google.cloud.firestore.WriteResult
import com.google.firebase.cloud.FirestoreClient
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.data.model.BaseModel

class FirebaseDatastoreRepository: DatastoreRepositoryContract {

    override fun createEntity(
        entityCollection: String,
        entityId: String,
        entity: BaseModel
    ): String? {
        val collectionFuture: ApiFuture<WriteResult> =
            FirestoreClient.getFirestore()
                .collection(entityCollection)
                .document(entityId)
                .set(entity.toMap())
        return collectionFuture.get().updateTime.toString()
    }

    override fun <T> createAndGetEntity(
        entityCollection: String,
        entityId: String,
        entity: BaseModel
    ): T? {
        FirestoreClient.getFirestore()
            .collection(entityCollection)
            .document(entityId)
            .set(entity.toMap())
        @Suppress("UNCHECKED_CAST")
        return entity as T
    }

    override fun updateEntity(
        entityCollection: String,
        entityId: String,
        entity: BaseModel
    ): String? {
        val collectionFuture: ApiFuture<WriteResult> =
            FirestoreClient.getFirestore()
                .collection(entityCollection)
                .document(entityId)
                .update(entity.toMap())
        return collectionFuture.get().updateTime.toString()
    }

    override fun <T> getAllInEntity(
        entityCollection: String,
        className: Class<T>
    ): List<T> {
        val entityList: MutableList<T> = ArrayList()
        val future: ApiFuture<QuerySnapshot> = FirestoreClient.getFirestore()
            .collection(entityCollection)
            .get()
        val documents = future.get().documents
        documents.forEach {
            it.toObject(className).let { entityItem -> entityList.add(entityItem) }
        }
        return entityList
    }

    override fun <T> getEntityFromId(
        entityCollection: String,
        entityId: String,
        className: Class<T>
    ): T? {
        val documentReference: DocumentReference = FirestoreClient.getFirestore().collection(entityCollection).document(entityId)
        val futureValue: ApiFuture<DocumentSnapshot> = documentReference.get()
        val document = futureValue.get()
        document?.toObject(className)?.let {
            return it
        }?: run {
            return null
        }
    }

    override fun <T> getEntityListFromProperty(
        entityCollection: String,
        propertyKey: String,
        propertyValue: Any,
        className: Class<T>
    ): List<T> {
        val entityList: MutableList<T> = ArrayList()
        val future: ApiFuture<QuerySnapshot> = FirestoreClient.getFirestore()
            .collection(entityCollection)
            .whereEqualTo(propertyKey, propertyValue)
            .get()
        val documents = future.get().documents
        documents.forEach {
            it.toObject(className).let { entityItem -> entityList.add(entityItem) }
        }
        return entityList
    }

    override fun <T> getEntityListFromProperties(
        entityCollection: String,
        propertiesMap: Map<String, Any>?,
        lessThanMap: Map<String, Any>?,
        greaterThanMap: Map<String, Any>?,
        orderByMap: Map<String, String>?,
        limit: Int?,
        className: Class<T>
    ): MutableList<T> {
        val entityList: MutableList<T> = ArrayList()
        val collectionEntity: CollectionReference = FirestoreClient.getFirestore()
            .collection(entityCollection)

        var collectionQuery: Query = collectionEntity

        propertiesMap?.keys?.forEach {
            collectionQuery = collectionQuery.whereEqualTo(it, propertiesMap[it])
        }
        lessThanMap?.keys?.forEach {
            lessThanMap[it]?.let { lessThanValue ->
                collectionQuery = collectionQuery.whereLessThan(it, lessThanValue)
            }
        }
        greaterThanMap?.keys?.forEach {
            greaterThanMap[it]?.let { greaterThanValue ->
                collectionQuery = collectionQuery.whereGreaterThan(it, greaterThanValue)
            }
        }
        orderByMap?.let {
            val orderByProperty = orderByMap["property"]
            val orderByDirection = orderByMap["direction"]
            if (orderByProperty != null && orderByDirection != null) {
                when (orderByDirection) {
                    "ASC" -> collectionQuery = collectionQuery.orderBy(orderByProperty, Query.Direction.ASCENDING)
                    "DESC" -> collectionQuery = collectionQuery.orderBy(orderByProperty, Query.Direction.DESCENDING)
                }
            }
        }
        limit?.let {
            collectionQuery = collectionQuery.limit(it)
        }

        val future: ApiFuture<QuerySnapshot> = collectionQuery.get()
        val documents = future.get().documents
        documents.forEach {
            it.toObject(className).let { entityItem -> entityList.add(entityItem) }
        }
        return entityList
    }

    override fun deleteEntity(entityCollection: String, entityId: String): String? {
        val collectionFuture: ApiFuture<WriteResult> =
            FirestoreClient.getFirestore()
                .collection(entityCollection)
                .document(entityId)
                .delete()
        return collectionFuture.get().updateTime.toString()
    }
}