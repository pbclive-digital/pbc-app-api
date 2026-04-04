package com.kavi.pbc.live.api.service

import com.kavi.pbc.live.api.AppProperties
import com.kavi.pbc.live.data.DataConstant
import com.kavi.pbc.live.data.util.DataUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.temporal.ChronoUnit

@Service
class FileService @Autowired constructor(appProperties: AppProperties) {

    private var fileStorageLocation: Path

    init {
        appProperties.fileDir.let { fileDir ->
            this.fileStorageLocation = Paths.get(fileDir).toAbsolutePath().normalize()

            try {
                Files.createDirectories(fileStorageLocation)
            } catch (ex: Exception) {
                throw Exception("Could not create the directory where the uploaded files will be stored.", ex)
            }
        }?: run {
            throw Exception("Could not create the directory where the uploaded files will be stored.")
        }
    }

    fun loadFileAsResource(fileName: String): Resource? {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw FileNotFoundException("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw Exception("File not found $fileName", ex)
        } catch (ex: FileNotFoundException) {
            throw Exception("File not found $fileName", ex)
        }
    }

    fun cleanExportDir(): Boolean {
        var isAnyFileDeleted = false
        try {
            Files.walk(fileStorageLocation)
                .sorted(Comparator.reverseOrder())
                .map { it.toFile() }
                .forEach {
                    val timeString = it.name.split("_").last().split(".")[0]
                    if(isNumeric(timeString)) {
                        val createdTime = timeString.toLong()
                        if (isOlder(createdTime)) {
                            it.delete()
                            isAnyFileDeleted = true
                        }
                    }
                }
        } catch (ex: Exception) {
            throw Exception("File not found", ex)
        }
        return isAnyFileDeleted
    }

    private fun isOlder(createdTime: Long): Boolean {
        val fiveMinutesAgo: Long = DataUtil.getOlderTimestamp(DataConstant.EXPORT_FILE_CLEAN, ChronoUnit.MINUTES)
        return createdTime < fiveMinutesAgo
    }

    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.toLongOrNull() != null
    }
}