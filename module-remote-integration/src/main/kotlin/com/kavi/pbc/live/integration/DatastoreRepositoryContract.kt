package com.kavi.pbc.live.com.kavi.pbc.live.integration

import com.kavi.pbc.live.data.model.BaseModel

interface DatastoreRepositoryContract {
    fun createEntity(entityCollection: String, entityId: String, entity: BaseModel): String?
    fun <T>createAndGetEntity(entityCollection: String, entityId: String, entity: BaseModel): T?
    fun updateEntity(entityCollection: String, entityId: String, entity: BaseModel): String?
    fun <T>getAllInEntity(entityCollection: String, className: Class<T>): List<T>
    fun <T>getAllInEntitySelectedAttributes(entityCollection: String, attributes: List<String>, className: Class<T>): List<T>
    fun <T>getEntityFromId(entityCollection: String, entityId: String, className: Class<T>): T?
    fun <T>getEntityListFromProperty(entityCollection: String,
                                     propertyKey: String, propertyValue: Any,
                                     className: Class<T>): List<T>
    fun <T>getEntityListFromProperties(entityCollection: String,
                                       propertiesMap: Map<String, Any>? = null,
                                       notInPropertiesMap: Map<String, Any>? = null,
                                       lessThanMap: Map<String, Any>? = null,
                                       greaterThanMap: Map<String, Any>? = null,
                                       orderByMap: Map<String, String>? = null,
                                       limit: Int? = null,
                                       className: Class<T>): MutableList<T>
    fun deleteEntity(entityCollection: String, entityId: String): String?

    // The transaction orchestrator
    fun <T> runInTransaction(action: (transaction: Any) -> T): T

    // Transaction-safe versions of methods used in your business logic
    fun <T> getEntityFromIdTx(transaction: Any, entityCollection: String, entityId: String, className: Class<T>): T?
    fun <T> getEntityListFromPropertiesTx(transaction: Any,
                                          entityCollection: String,
                                          propertiesMap: Map<String, Any>? = null,
                                          notInPropertiesMap: Map<String, Any>? = null,
                                          lessThanMap: Map<String, Any>? = null,
                                          greaterThanMap: Map<String, Any>? = null,
                                          orderByMap: Map<String, String>? = null,
                                          limit: Int? = null,
                                          className: Class<T>): MutableList<T>
    fun updateEntityTx(transaction: Any, entityCollection: String, entityId: String, entity: BaseModel)
    fun createEntityTx(transaction: Any, entityCollection: String, entityId: String, entity: BaseModel)
}