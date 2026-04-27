package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.AppProperties
import com.kavi.pbc.live.api.data.dto.BaseResponse
import com.kavi.pbc.live.api.data.dto.Error
import com.kavi.pbc.live.api.data.dto.Status
import com.kavi.pbc.live.api.data.model.EmailTemplateType
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.com.kavi.pbc.live.integration.DatastoreRepositoryContract
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.DatastoreConstant
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.datastore.FirebaseDatastoreRepository
import com.kavi.pbc.live.csv.CsvEmailItemGenerator
import com.kavi.pbc.live.data.model.broadcast.EmailBroadcastMessage
import com.kavi.pbc.live.data.model.broadcast.EmailNewEventMessage
import com.kavi.pbc.live.data.model.email.EmailGroup
import com.kavi.pbc.live.data.model.email.EmailGroupHeading
import com.kavi.pbc.live.data.model.email.EmailItem
import jakarta.mail.MessagingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets


@Service
class EmailService(
    private val allBroadcasters: List<JavaMailSender>
) {

    @Autowired
    lateinit var templateEngine: TemplateEngine

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var logger: AppLogger

    private var datastoreRepositoryContract: DatastoreRepositoryContract = FirebaseDatastoreRepository()

    @Value("classpath:email-group-template/email-group-csv-file-template.csv")
    var emailGroupCSVTemplateResource: Resource? = null

    fun createEmailGroup(emailGroup: EmailGroup): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.createEntity(DatastoreConstant.EMAIL_GROUP_COLLECTION,
                    emailGroup.id, emailGroup), null))
    }

    fun downloadEmailGroupTemplate(fileName: String): ResponseEntity<Resource>? {
        val header = HttpHeaders()
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}");

        return ResponseEntity.ok()
            .headers(header)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(emailGroupCSVTemplateResource)
    }

    fun createEmailGroupFromFile(groupName: String, file: MultipartFile): ResponseEntity<BaseResponse<String>> {
        fileFromMultipartFile(file)?.let {
            val emailItemList = CsvEmailItemGenerator.shared.readCsvFile(it)

            // Delete generated file
            it.delete()

            // Create EmailGroup
            val emailGroup = EmailGroup(
                name = groupName,
                emails = emailItemList
            )

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse(Status.SUCCESS,
                    datastoreRepositoryContract.createEntity(DatastoreConstant.EMAIL_GROUP_COLLECTION,
                        emailGroup.id, emailGroup), null))
        }?: run {
            val errorList = listOf(Error("File conversion error. Please follow the given format and upload a new questions file."))

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse(Status.ERROR, null, errorList))
        }
    }

    fun getAllEmailGroups(): ResponseEntity<BaseResponse<List<EmailGroupHeading>>>? {
        val attributes = listOf(
            "id", "name"
        )

        val emailGroupHeadingList = datastoreRepositoryContract.getAllInEntitySelectedAttributes(DatastoreConstant.EMAIL_GROUP_COLLECTION,
            attributes, EmailGroupHeading::class.java)

        return if (emailGroupHeadingList.isNotEmpty()) {
            ResponseEntity.ok(BaseResponse(Status.SUCCESS, emailGroupHeadingList, null))
        } else {
            ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun getEmailGroupById(groupId: String): ResponseEntity<BaseResponse<EmailGroup>>? {
        emailGroupById(groupId = groupId)?.let { emailGroup ->
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS, emailGroup, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error(HttpStatus.NOT_FOUND.toString()))
                ))
        }
    }

    fun addEmailsToGroup(groupId: String, emailList: List<EmailItem>): ResponseEntity<BaseResponse<EmailGroup>>? {
        emailGroupById(groupId = groupId)?.let { emailGroup ->
            val mergeResultSet = LinkedHashSet<EmailItem>(emailGroup.emails)
            mergeResultSet.addAll(emailList)

            emailGroup.emails = ArrayList(mergeResultSet)
            datastoreRepositoryContract.updateEntity(DatastoreConstant.EMAIL_GROUP_COLLECTION,
                groupId, emailGroup)
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS,
                emailGroup, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error("Email group not found with given ID `$groupId`"))
                ))
        }
    }

    fun removeEmailsToGroup(groupId: String, emailList: List<EmailItem>): ResponseEntity<BaseResponse<EmailGroup>>? {
        emailGroupById(groupId = groupId)?.let { emailGroup ->
            emailGroup.emails.removeAll(emailList.toSet())
            datastoreRepositoryContract.updateEntity(DatastoreConstant.EMAIL_GROUP_COLLECTION,
                groupId, emailGroup)
            return ResponseEntity.ok(BaseResponse(Status.SUCCESS,
                emailGroup, null))
        }?: run {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse(Status.ERROR, null, listOf(
                    Error("Email group not found with given ID `$groupId`"))
                ))
        }
    }

    fun getEmailListOfGroup(groupId: String):
            ResponseEntity<BaseResponse<List<EmailItem>>>? {

        emailGroupById(groupId = groupId)?.let { emailGroup ->
            return ResponseEntity.ok(
                BaseResponse(
                    Status.SUCCESS,
                    emailGroup.emails, null
                )
            )
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                BaseResponse(
                    Status.ERROR, null, listOf(
                        Error("Email group not found with given ID `$groupId`")
                    )
                )
            )
    }

    fun getEmailGroupsFromEmail(email: String):
            ResponseEntity<BaseResponse<List<EmailGroupHeading>>>? {
        val emailGroupHeadings = mutableListOf<EmailGroupHeading>()

        val allEmailGroups = datastoreRepositoryContract.getAllInEntity(DatastoreConstant.EMAIL_GROUP_COLLECTION,
            EmailGroup::class.java)

        allEmailGroups.forEach { emailGroup ->
            if (emailGroup.emails.any { it.email == email }) {
                emailGroupHeadings.add(EmailGroupHeading(emailGroup.id, emailGroup.name))
            }
        }

        if (emailGroupHeadings.isNotEmpty()) {
            return ResponseEntity.ok(
                BaseResponse(
                    Status.SUCCESS,
                    emailGroupHeadings, null
                )
            )
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    BaseResponse(
                        Status.ERROR, null, listOf(
                            Error("This email: `$email` is not containing in any email group.")
                        )
                    )
                )
        }
    }

    fun deleteEmailGroup(groupId: String): ResponseEntity<BaseResponse<String>>? {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(BaseResponse(Status.SUCCESS,
                datastoreRepositoryContract.deleteEntity(DatastoreConstant.EMAIL_GROUP_COLLECTION, groupId),
                null))
    }

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

    fun sendEmailToSelectedGroups(emailNewEventMessage: EmailNewEventMessage,
                                  emailGroups: List<EmailGroupHeading>) {
        val mergeResultSet = LinkedHashSet<String>()

        emailGroups.forEach { group ->
            datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EMAIL_GROUP_COLLECTION,
                group.id, EmailGroup::class.java)?.let { emailGroup ->
                    val emailList = mutableListOf<String>()
                    emailGroup.emails.forEach { emailItem ->
                        emailList.add(emailItem.email)
                    }
                mergeResultSet.addAll(emailList)
            }
        }

        val emailList = mergeResultSet.toList()

        try {
            if (emailList.isNotEmpty()) {
                sendInBatches(
                    recipients = emailList,
                    emailSubject = emailNewEventMessage.subject,
                    emailTemplateType = EmailTemplateType.NEW_EVENT,
                    emailTemplateContent = mapOf(
                        "title" to emailNewEventMessage.title,
                        "message" to emailNewEventMessage.message,
                        "description" to emailNewEventMessage.eventDescription,
                        "eventUrl" to emailNewEventMessage.eventUrl,
                    )
                )
            }
        } catch (ex: MessagingException) {
            // Handle error (logging is recommended here)
            ex.printStackTrace()
        }
    }

    fun sendNewEventEmail(
        emailNewEventMessage: EmailNewEventMessage
    ) {
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
                            "description" to emailNewEventMessage.eventDescription,
                            "eventUrl" to emailNewEventMessage.eventUrl,
                        )
                    )
                }
            }
        } catch (ex: MessagingException) {
            // Handle error (logging is recommended here)
            ex.printStackTrace()
        }
    }

    private fun emailGroupById(groupId: String): EmailGroup? {
        datastoreRepositoryContract.getEntityFromId(DatastoreConstant.EMAIL_GROUP_COLLECTION,
            groupId, EmailGroup::class.java)?.let { emailGroup ->
                return emailGroup
        }

        return null
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
            var broadcasterIndex: Int
            var broadcasterIterationCount: Int
            chunks.forEachIndexed { index, batch ->
                /**
                 * This logic is to iterate the email senders via braches
                 */
                broadcasterIterationCount = index / appProperties.mailBroadcasterCount.toInt()
                broadcasterIndex = index - appProperties.mailBroadcasterCount.toInt() * broadcasterIterationCount
                batch.forEach { email ->
                    sendHtmlEmail(
                        allBroadcasters[broadcasterIndex],
                        email,
                        emailSubject,
                        emailTemplateContent,
                        emailTemplateType
                    )
                }

                // Non-blocking delay between batches
                if (index < chunks.size - 1) {
                    delay(pauseMillis)
                }
            }
        }
    }

    private fun sendHtmlEmail(
        broadcaster: JavaMailSender,
        recipientEmail: String,
        emailSubject: String,
        emailTemplateContent: Map<String, String>,
        emailTemplateType: EmailTemplateType
    ) {
        try {
            val mimeMessage = broadcaster.createMimeMessage()
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
            helper.setFrom(getSenderUserName(broadcaster)?: "pbclive.digital@gmail.com")
            helper.setTo(recipientEmail)
            helper.setSubject(emailSubject)

            // 3. Attach the processed HTML content
            // The 'true' flag indicates this is an HTML email
            helper.setText(htmlContent, true)

            // 4. Add the inline image
            val res = ClassPathResource("static/images/image_pbc.png")
            helper.addInline("logo", res)

            broadcaster.send(mimeMessage)
        } catch (ex: Exception) {
            logger.printError(message = ex.localizedMessage, throwable = ex, EmailService::class.java)
        }
    }

    /**
     * Convert Multipart file to File
     */
    private fun fileFromMultipartFile(multipartFile: MultipartFile): File? {
        try {
            val tempFilePath = "./build/tmp"

            multipartFile.originalFilename?.let {
                val convFile = File("$tempFilePath/$it")

                convFile.createNewFile()
                val fos = FileOutputStream(convFile)
                fos.write(multipartFile.bytes)
                fos.close()

                return convFile
            } ?: run {
                return null
            }
        } catch (ex: FileNotFoundException) {
            return null
        } catch (ex: IOException) {
            return null
        }
    }

    private fun getSenderUserName(emailSender: JavaMailSender): String? {
        return (emailSender as? JavaMailSenderImpl)?.username
    }
}