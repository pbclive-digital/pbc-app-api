package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.AppProperties
import com.kavi.pbc.live.api.data.dto.BaseResponse
import com.kavi.pbc.live.api.data.dto.Error
import com.kavi.pbc.live.api.data.dto.Status
import com.kavi.pbc.live.api.data.model.EmailTemplateType
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.model.broadcast.EmailBroadcastMessage
import com.kavi.pbc.live.data.model.broadcast.EmailNewEventMessage
import jakarta.mail.MessagingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.nio.charset.StandardCharsets


@Service
class EmailService {

    @Autowired
    lateinit var mailSender: JavaMailSender

    @Autowired
    lateinit var templateEngine: TemplateEngine

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var logger: AppLogger

    fun sendBroadcastEmail(
        emailBroadcastMessage: EmailBroadcastMessage
    ): ResponseEntity<BaseResponse<String>>? {
        try {
            // Broadcast to all available emails by batches
            userService.getAllUserEmails()?.let { userEmails ->
                if (userEmails.isNotEmpty()) {
                    sendInBatches(
                        recipients = userEmails,
                        emailSubject = emailBroadcastMessage.subject,
                        emailTemplateType = EmailTemplateType.BROADCAST,
                        emailTemplateContent = mapOf(
                            "title" to emailBroadcastMessage.title,
                            "message" to emailBroadcastMessage.message,
                        )
                    )
                }
            }

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    BaseResponse(
                        Status.SUCCESS,
                        "Broadcast Email Initiated. This is not an confirmation of email broadcasted.",
                        null
                    )
                )
        } catch (ex: MessagingException) {
            // Handle error (logging is recommended here)
            ex.printStackTrace()

            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    BaseResponse(
                        Status.ERROR, null, listOf(
                            Error(message = ex.localizedMessage)
                        )
                    )
                )
        }
    }

    fun sendNewEventEmail(
        emailNewEventMessage: EmailNewEventMessage
    ): ResponseEntity<BaseResponse<String>>? {
        try {
            // Broadcast to all available emails by batches
            userService.getAllUserEmails()?.let { userEmails ->
                if (userEmails.isNotEmpty()) {
                    sendInBatches(
                        recipients = userEmails,
                        emailSubject = emailNewEventMessage.subject,
                        emailTemplateType = EmailTemplateType.NEW_EVENT,
                        emailTemplateContent = mapOf(
                            "title" to emailNewEventMessage.title,
                            "message" to emailNewEventMessage.message,
                            "eventUrl" to emailNewEventMessage.eventUrl,
                        )
                    )
                }
            }

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                    BaseResponse(
                        Status.SUCCESS,
                        "Broadcast Email Initiated. This is not an confirmation of email broadcasted.",
                        null
                    )
                )
        } catch (ex: MessagingException) {
            // Handle error (logging is recommended here)
            ex.printStackTrace()

            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    BaseResponse(
                        Status.ERROR, null, listOf(
                            Error(message = ex.localizedMessage)
                        )
                    )
                )
        }
    }

    private fun sendInBatches(
        recipients: List<String>,
        emailSubject: String,
        emailTemplateType: EmailTemplateType,
        emailTemplateContent: Map<String, String>,
    ) {
        val batchSize = 10
        val pauseMillis = 5000L

        // Break the list into chunks of 50
        val chunks = recipients.chunked(batchSize)

        CoroutineScope(Dispatchers.IO).launch {
            chunks.forEachIndexed { index, batch ->
                batch.forEach { email ->
                    sendHtmlEmail(email, emailSubject, emailTemplateContent, emailTemplateType)
                }

                // Non-blocking delay between batches
                if (index < chunks.size - 1) {
                    delay(pauseMillis)
                }
            }
        }
    }

    private fun sendHtmlEmail(
        recipientEmail: String,
        emailSubject: String,
        emailTemplateContent: Map<String, String>,
        emailTemplateType: EmailTemplateType
    ) {
        try {
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
            )

            // 1. Prepare the Thymeleaf Context & Process the Template (template name without .html extension)
            val context = Context()
            val htmlContent = when (emailTemplateType) {
                EmailTemplateType.BROADCAST -> {
                    emailTemplateContent.forEach { (key, value) ->
                        context.setVariable(key, value)
                    }

                    templateEngine.process("email/broadcast-message", context)
                }
                EmailTemplateType.NEW_EVENT -> {
                    emailTemplateContent.forEach { (key, value) ->
                        context.setVariable(key, value)
                    }

                    templateEngine.process("email/new-event", context)
                }
            }

            // 2. Set Email Metadata
            helper.setFrom(appProperties.pbcBroadcastEmail)
            helper.setTo(recipientEmail)
            helper.setSubject(emailSubject)

            // 3. Attach the processed HTML content
            // The 'true' flag indicates this is an HTML email
            helper.setText(htmlContent, true)

            // 4. Add the inline image
            val res = ClassPathResource("static/images/image_pbc.png")
            helper.addInline("logo", res)

            mailSender.send(mimeMessage)
        } catch (ex: Exception) {
            logger.printError(message = ex.localizedMessage, throwable = ex, EmailService::class.java)
        }
    }
}