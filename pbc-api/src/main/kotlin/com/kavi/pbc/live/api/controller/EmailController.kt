package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.data.dto.BaseResponse
import com.kavi.pbc.live.api.service.EmailService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.email.EmailGroup
import com.kavi.pbc.live.data.model.email.EmailGroupHeading
import com.kavi.pbc.live.data.model.email.EmailItem
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
@RequestMapping("/email-group")
class EmailController(private val emailService: EmailService) {

    @Autowired
    lateinit var logger: AppLogger

    @PostMapping("/create")
    fun createEmailGroup(@Valid @RequestBody emailGroup: EmailGroup): ResponseEntity<BaseResponse<String>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/email-group/create]", EmailController::class.java)

        val response = emailService.createEmailGroup(emailGroup)
        logger.printResponseInfo(response, EmailController::class.java)

        return response
    }

    @GetMapping("/get/all")
    fun getEmailGroups(): ResponseEntity<BaseResponse<List<EmailGroupHeading>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/email-group/get/all]", EmailController::class.java)

        val response = emailService.getAllEmailGroups()
        logger.printResponseInfo(response, EmailController::class.java)

        return response
    }

    @GetMapping("/get/{group-id}")
    fun getEmailGroup(@PathVariable(value = "group-id") groupId: String): ResponseEntity<BaseResponse<EmailGroup>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: GET: [/email-group/get/$groupId]", EmailController::class.java)

        val response = emailService.getEmailGroupById(groupId)
        logger.printResponseInfo(response, EmailController::class.java)

        return response
    }

    @GetMapping("/get/email-list/{group-id}")
    fun getEmailListOfGroup(@PathVariable(value = "group-id") groupId: String):
            ResponseEntity<BaseResponse<List<EmailItem>>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: POST: [/email-group/get/email-list/$groupId]",
            EmailController::class.java)

        val response = emailService.getEmailListOfGroup(groupId)
        logger.printResponseInfo(response, EmailController::class.java)

        return response
    }

    @PutMapping("/add/emails/{group-id}")
    fun addEmailsToEmailGroup(@PathVariable(value = "group-id") groupId: String, @Valid @RequestBody emailList: List<EmailItem>): ResponseEntity<BaseResponse<EmailGroup>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/email-group/add/emails/$groupId]", EmailController::class.java)

        val response = emailService.addEmailsToGroup(groupId, emailList)
        logger.printResponseInfo(response, EmailController::class.java)

        return response
    }

    @PutMapping("/remove/emails/{group-id}")
    fun removeEmailsFromEmailGroup(@PathVariable(value = "group-id") groupId: String, @Valid @RequestBody emailList: List<EmailItem>): ResponseEntity<BaseResponse<EmailGroup>>? {
        logger.printSeparator()
        logger.printInfo("REQUEST MAPPING: PUT: [/email-group/add/emails/$groupId]", EmailController::class.java)

        val response = emailService.removeEmailsToGroup(groupId, emailList)
        logger.printResponseInfo(response, EmailController::class.java)

        return response
    }
}