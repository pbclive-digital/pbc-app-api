package com.kavi.pbc.live.api.task

import com.kavi.pbc.live.api.service.AppointmentService
import com.kavi.pbc.live.api.util.AppLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
class AppointmentStatusCheckTask {

    @Autowired
    lateinit var logger: AppLogger

    @Autowired
    lateinit var appointmentService: AppointmentService

    @Scheduled(cron="0 40 00 * * *", zone="America/New_York") // Running this on every data at 00:05 midnight in EST time.
    fun appointmentStatusCheckAndChange() {
        val result = appointmentService.updateAppointmentStatusAccordingToDate()
        logger.printSeparator()
        logger.printInfo("TASK: EXECUTED TIME: ${Date()}", AppointmentStatusCheckTask::class.java)
        logger.printInfo("TASK: AppointmentStatusCheckTask: $result", AppointmentStatusCheckTask::class.java)
    }
}