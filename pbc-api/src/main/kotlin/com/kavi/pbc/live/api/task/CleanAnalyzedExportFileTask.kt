package com.kavi.pbc.live.api.task

import com.kavi.pbc.live.api.service.FileService
import com.kavi.pbc.live.api.util.AppLogger
import com.kavi.pbc.live.data.DataConstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
class CleanAnalyzedExportFileTask {

    @Autowired
    lateinit var logger: AppLogger

    @Autowired
    lateinit var fileService: FileService

    @Scheduled(fixedRate = (DataConstant.EXPORT_FILE_CLEAN * 60 * 1000).toLong()) // Frequency from minutes
    fun cleanAnalyzedExportFile() {
        val isAnyFileDeleted = fileService.cleanExportDir()
        logger.printSeparator()
        logger.printInfo("TASK: CleanAnalyzedExportFileTask: EXECUTED TIME: ${Date()}", CleanAnalyzedExportFileTask::class.java)
        logger.printInfo("TASK: CleanAnalyzedExportFileTask: isAnyFileDeleted: [$isAnyFileDeleted]", CleanAnalyzedExportFileTask::class.java)
    }
}