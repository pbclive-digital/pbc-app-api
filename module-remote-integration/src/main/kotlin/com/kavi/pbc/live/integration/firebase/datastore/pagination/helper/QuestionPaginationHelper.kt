package com.kavi.pbc.live.integration.firebase.datastore.pagination.helper

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.firebase.cloud.FirestoreClient
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.data.DataConstant.QUESTION_PAGE_SIZE
import com.kavi.pbc.live.data.model.question.Question
import com.kavi.pbc.live.integration.firebase.datastore.pagination.PaginationHolder
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationResponse

class QuestionPaginationHelper {

    fun getAllQuestionList(previousPageLastDocKey: String?, isMonk: Boolean): PaginationResponse<Question> {
        val firestore: Firestore = FirestoreClient.getFirestore()
        val entityList = mutableListOf<Question>()
        var key: String? = null

        previousPageLastDocKey?.let {
            var previousPageLastDoc = PaginationHolder.shared.getFromMap(it)
            val nextPage = previousPageLastDoc?.let {
                getQuestionListNextPageQuery(firestore, previousPageLastDoc, isMonk)
            }?: run {
                getQuestionListFirstPageQuery(firestore, isMonk)
            }

            val nextPageDocuments = nextPage.get().get().documents

            if (nextPageDocuments.isNotEmpty()) {
                nextPageDocuments.forEach { document ->
                    entityList.add(document.toObject(Question::class.java))
                }
                previousPageLastDoc = nextPageDocuments.last()
                key = PaginationHolder.shared.addToMap(it, previousPageLastDoc)
            }
        }?: run {
            val firstPage = getQuestionListFirstPageQuery(firestore, isMonk)
            val firstPageDocuments = firstPage.get().get().documents

            if (firstPageDocuments.isNotEmpty()) {
                firstPageDocuments.forEach { document ->
                    entityList.add(document.toObject(Question::class.java))
                }
                val firstPageLastDoc = firstPageDocuments.last()
                key = PaginationHolder.shared.addToMap(firstPageLastDoc)
            } else {
                key = "NO-NEXT-PAGE"
            }
        }

        return PaginationResponse(entityList, key)
    }

    private fun getQuestionListFirstPageQuery(firestore: Firestore, isMonk: Boolean): Query {
        return if (isMonk) {
            firestore.collection(DatastoreConstant.QUESTION_COLLECTION)
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(QUESTION_PAGE_SIZE)
        } else {
            firestore.collection(DatastoreConstant.QUESTION_COLLECTION)
                .whereEqualTo("privacy", "PUBLIC")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(QUESTION_PAGE_SIZE)
        }

    }

    private fun getQuestionListNextPageQuery(firestore: Firestore, lastDoc: QueryDocumentSnapshot, isMonk: Boolean): Query {
        return if (isMonk) {
            firestore.collection(DatastoreConstant.QUESTION_COLLECTION)
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .startAfter(lastDoc)
                .limit(QUESTION_PAGE_SIZE)
        } else {
            firestore.collection(DatastoreConstant.QUESTION_COLLECTION)
                .whereEqualTo("privacy", "PUBLIC")
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .startAfter(lastDoc)
                .limit(QUESTION_PAGE_SIZE)
        }

    }
}