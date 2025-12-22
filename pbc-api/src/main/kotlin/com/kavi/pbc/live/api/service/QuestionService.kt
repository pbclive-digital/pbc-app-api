package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.dto.Error
import com.kavi.pbc.live.api.dto.Status
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.data.model.news.News
import com.kavi.pbc.live.data.model.question.Answer
import com.kavi.pbc.live.data.model.question.Question
import com.kavi.pbc.live.integration.firebase.datastore.pagination.helper.QuestionPaginationHelper
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationRequest
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class QuestionService {

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()
    private val questionPaginationHelper = QuestionPaginationHelper()

    fun createQuestion(question: Question): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.QUESTION_COLLECTION,
                    question.id, question), null))
    }

    fun addAnswerComment(questionId: String, answer: Answer): ResponseEntity<BaseResponse<Question>>? {
        val question = datastoreRepositoryContract.getEntityFromId(
            entityCollection = DatastoreConstant.QUESTION_COLLECTION,
            entityId = questionId,
            className = Question::class.java)

        question?.let {
            it.answerList.add(answer)

            datastoreRepositoryContract.updateEntity(
                entityCollection = DatastoreConstant.QUESTION_COLLECTION,
                entityId = questionId,
                entity = question
            )

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse(Status.SUCCESS, it, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString())
                )))
        }
    }

    fun getAllQuestionList(paginationRequest: PaginationRequest? = null):
            ResponseEntity<BaseResponse<PaginationResponse<Question>>>? {
        val response = questionPaginationHelper.getAllQuestionList(paginationRequest?.previousPageLastDocKey)

        return if (response.entityList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, response, null))
        } else {
            if (response.previousPageLastDocKey == "NO-NEXT-PAGE") {
                ResponseEntity
                    .status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .body(BaseResponse(Status.ERROR, null, listOf(
                        Error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.toString()))
                    ))
            } else {
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse(Status.ERROR, null, listOf(
                        Error(HttpStatus.NOT_FOUND.toString()))
                    ))
            }
        }
    }

    fun getUserQuestionList(userId: String): ResponseEntity<BaseResponse<List<Question>>>? {

        val properties = mapOf(
            "authorId" to userId
        )

        val orderBy = mapOf(
            "property" to "createdTime",
            "direction" to "ASC"
        )

        val userQuestionList = datastoreRepositoryContract.getEntityListFromProperties(
            entityCollection = DatastoreConstant.QUESTION_COLLECTION,
            propertiesMap = properties,
            orderByMap = orderBy,
            className = Question::class.java
        )

        return if (userQuestionList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, userQuestionList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun updateQuestion(questionId: String, question: Question): ResponseEntity<BaseResponse<Question>> {
        datastoreRepositoryContract.updateEntity(DatastoreConstant.QUESTION_COLLECTION,
            questionId, question)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                question, null))
    }
}