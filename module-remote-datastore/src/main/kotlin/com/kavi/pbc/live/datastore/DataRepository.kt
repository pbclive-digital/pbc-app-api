package com.kavi.pbc.live.com.kavi.pbc.live.datastore

import com.kavi.pbc.live.data.model.BaseModel

interface DataRepository {
    fun createEntity(entityCollection: String, entityId: String, entity: BaseModel): String?
    fun <T>createAndGetEntity(entityCollection: String, entityId: String, entity: BaseModel): T?
    fun updateEntity(entityCollection: String, entityId: String, entity: BaseModel): String?
    fun <T>getAllInEntity(entityCollection: String, className: Class<T>): List<T>
    fun <T>getEntityFromId(entityCollection: String, entityId: String, className: Class<T>): T?
    fun <T>getEntityListFromProperty(entityCollection: String,
                                     propertyKey: String, propertyValue: Any,
                                     className: Class<T>): List<T>
    fun <T>getEntityListFromProperties(entityCollection: String,
                                       propertiesMap: Map<String, Any>? = null,
                                       lessThanMap: Map<String, Any>? = null,
                                       greaterThanMap: Map<String, Any>? = null,
                                       orderByMap: Map<String, String>? = null,
                                       limit: Int? = null,
                                       className: Class<T>): MutableList<T>
    fun deleteEntity(entityCollection: String, entityId: String): String?
}