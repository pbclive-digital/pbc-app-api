package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.dto.BaseResponse
import com.kavi.pbc.live.api.service.QuestionService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.question.Answer
import com.kavi.pbc.live.data.model.question.Question
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationRequest
import com.kavi.pbc.live.integration.firebase.datastore.pagination.model.PaginationResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/question")
class QuestionController(private val questionService: QuestionService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/create")
    fun createQuestion(@Valid @RequestBody question: Question): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/question/create]", QuestionController::class.java)

        val response = questionService.createQuestion(question)
        logger.printResponseInfo(response, QuestionController::class.java)

        return response
    }

    @PutMapping("/add/comment/{question-id}")
    fun addAnswerComment(@PathVariable(value = "question-id") questionId: String,
                         @Valid @RequestBody answer: Answer): ResponseEntity<BaseResponse<Question>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/question/add/comment/$questionId]", QuestionController::class.java)

        val response = questionService.addAnswerComment(questionId, answer)
        logger.printResponseInfo(response, QuestionController::class.java)

        return response
    }

    @PostMapping("/get/all")
    fun getAllQuestionList(@Valid @RequestBody paginationRequest: PaginationRequest?):
            ResponseEntity<BaseResponse<PaginationResponse<Question>>>?{
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/question/get/all]", QuestionController::class.java)

        val response = questionService.getAllQuestionList(paginationRequest)
        logger.printResponseInfo(response, QuestionController::class.java)

        return response
    }

    @GetMapping("/get/user/{user-id}")
    fun getGivenUserQuestionList(@PathVariable(value = "user-id") userId: String): ResponseEntity<BaseResponse<List<Question>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/question/get/user/$userId]", QuestionController::class.java)

        val response = questionService.getUserQuestionList(userId)
        logger.printResponseInfo(response, QuestionController::class.java)

        return response
    }
}