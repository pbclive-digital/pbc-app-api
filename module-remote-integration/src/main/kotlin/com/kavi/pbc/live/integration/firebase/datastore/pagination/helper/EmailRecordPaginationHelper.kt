package com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.pagination.helper

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.firebase.cloud.FirestoreClient
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.data.DataConstant.EMAIL_RECORD_PAGE_SIZE
import com.kavi.pbc.live.data.model.broadcast.record.EmailRecord
import com.kavi.pbc.live.integration.firebase.datastore.pagination.PaginationHolder
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationResponse

class EmailRecordPaginationHelper {

    fun getAllEmailRecordList(previousPageLastDocKey: String?): PaginationResponse<EmailRecord> {
        val firestore: Firestore = FirestoreClient.getFirestore()
        val entityList = mutableListOf<EmailRecord>()
        var key: String? = null

        previousPageLastDocKey?.let {
            var previousPageLastDoc = PaginationHolder.shared.getFromMap(it)
            val nextPage = previousPageLastDoc?.let {
                getEmailRecordListNextPageQuery(firestore, previousPageLastDoc)
            }?: run {
                getEmailRecordListFirstPageQuery(firestore)
            }

            val nextPageDocuments = nextPage.get().get().documents

            if (nextPageDocuments.isNotEmpty()) {
                nextPageDocuments.forEach { document ->
                    entityList.add(document.toObject(EmailRecord::class.java))
                }
                previousPageLastDoc = nextPageDocuments.last()
                key = PaginationHolder.shared.addToMap(it, previousPageLastDoc)
            }
        }?: run {
            val firstPage = getEmailRecordListFirstPageQuery(firestore)
            val firstPageDocuments = firstPage.get().get().documents

            if (firstPageDocuments.isNotEmpty()) {
                firstPageDocuments.forEach { document ->
                    entityList.add(document.toObject(EmailRecord::class.java))
                }
                val firstPageLastDoc = firstPageDocuments.last()
                key = PaginationHolder.shared.addToMap(firstPageLastDoc)
            } else {
                key = "NO-NEXT-PAGE"
            }
        }

        return PaginationResponse(entityList, key)
    }

    private fun getEmailRecordListFirstPageQuery(firestore: Firestore): Query {
        return firestore.collection(DatastoreConstant.EMAIL_RECORD_COLLECTION)
            .orderBy("sentTime", Query.Direction.DESCENDING)
            .limit(EMAIL_RECORD_PAGE_SIZE)
    }

    private fun getEmailRecordListNextPageQuery(firestore: Firestore, lastDoc: QueryDocumentSnapshot): Query {
        return firestore.collection(DatastoreConstant.EMAIL_RECORD_COLLECTION)
            .orderBy("sentTime", Query.Direction.DESCENDING)
            .startAfter(lastDoc)
            .limit(EMAIL_RECORD_PAGE_SIZE)
    }
}