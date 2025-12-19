package com.kavi.pbc.live.integration.firebase.datastore.pagination

import com.google.cloud.firestore.QueryDocumentSnapshot
import com.kavi.pbc.live.data.DataConstant.PAGINATION_KEY_LENGTH
import com.kavi.pbc.live.data.DataConstant.PAGINATION_LIFE_TIME
import com.kavi.pbc.live.data.util.DataUtil
import com.kavi.pbc.live.data.util.DataUtil.getCurrentTimestamp
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationData
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class PaginationHolder {

    private val paginationDocumentSnapshotMap = ConcurrentHashMap<String, PaginationData>()

    companion object {
        val shared: PaginationHolder = PaginationHolder()
    }

    fun addToMap(key: String, value: QueryDocumentSnapshot): String {
        val paginationData = PaginationData(key,
            value, getCurrentTimestamp())
        paginationDocumentSnapshotMap[key] = paginationData

        return key
    }

    fun addToMap(value: QueryDocumentSnapshot): String {
        val key = generateKey()
        val paginationData = PaginationData(key,
            value, getCurrentTimestamp())
        paginationDocumentSnapshotMap[key] = paginationData

        return key
    }

    fun removeFromMap(key: String) {
        paginationDocumentSnapshotMap.remove(key)
    }

    fun getFromMap(key: String): QueryDocumentSnapshot? {
        return paginationDocumentSnapshotMap.remove(key)?.lastDocumentSnapshot
    }

    fun cleanMap(): Boolean {
        var isRemoved = false
        paginationDocumentSnapshotMap.forEach { (key, value) ->
            if (isOlder(value.createdTime)) {
                isRemoved = true
                removeFromMap(key)
            }
        }
        return isRemoved
    }

    private fun generateKey(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return ThreadLocalRandom.current()
            .ints(PAGINATION_KEY_LENGTH.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }

    private fun isOlder(createdTime: Long): Boolean {
        val twoMinutesAgo: Long = DataUtil.getOlderTimestamp(PAGINATION_LIFE_TIME, ChronoUnit.MINUTES)
        return createdTime < twoMinutesAgo
    }
}