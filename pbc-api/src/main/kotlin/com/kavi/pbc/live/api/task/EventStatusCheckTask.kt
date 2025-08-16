package com.kavi.pbc.live.api.task

import com.kavi.pbc.live.api.service.EventService
import com.kavi.pbc.live.api.util.AppLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
class EventStatusCheckTask {

    @Autowired
    lateinit var logger: AppLogger

    @Autowired
    lateinit var eventService: EventService

    @Scheduled(cron="0 35 00 * * *", zone="America/New_York") // Running this on every data at 00:05 midnight in EST time.
    fun eventStatusCheckAndChange() {
        val result = eventService.updateEventStatusAccordingToDate()
        logger.printSeparator()
        logger.printInfo("TASK: EXECUTED TIME: ${Date()}", EventStatusCheckTask::class.java)
        logger.printInfo("TASK: EventStatusCheckTask: $result", EventStatusCheckTask::class.java)
    }
}