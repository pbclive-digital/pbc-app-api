package com.kavi.pbc.live.api.task

import com.kavi.pbc.live.api.service.NewsService
import com.kavi.pbc.live.api.util.AppLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
class ArchiveNewsTask {

    @Autowired
    lateinit var logger: AppLogger

    @Autowired
    lateinit var newsService: NewsService

    @Scheduled(cron="0 45 00 * * *", zone="America/New_York") // Running this on every data at 00:05 midnight in EST time.
    fun archiveOldNews() {
        val result = newsService.updateNewsStatusAccordingToDate()
        logger.printSeparator()
        logger.printInfo("TASK: EXECUTED TIME: ${Date()}", ArchiveNewsTask::class.java)
        logger.printInfo("TASK: AppointmentStatusCheckTask: $result", ArchiveNewsTask::class.java)
    }
}