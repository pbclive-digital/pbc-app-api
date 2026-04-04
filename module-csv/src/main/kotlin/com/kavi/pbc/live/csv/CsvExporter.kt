package com.kavi.pbc.live.csv

import com.kavi.pbc.live.csv.config.ExportConfig
import com.kavi.pbc.live.data.model.event.potluck.EventPotluck
import com.kavi.pbc.live.data.model.event.register.EventRegistration
import com.kavi.pbc.live.data.model.event.signup.sheet.EventSignUpSheet
import java.io.File

class CsvExporter {

    private var exportConfig: ExportConfig? = null

    companion object {
        var shared: CsvExporter = CsvExporter()
    }

    fun initiate(config: ExportConfig) {
        exportConfig = config
    }

    fun exportEventRegistrationAsCsv(eventName: String, eventRegistration: EventRegistration): File {
        // Registration output - list
        val fileOutput = mutableListOf<List<String>>()

        // Registration Columns
        fileOutput.add(listOf(
            "Name", "Contact Number", "Address", "Status"
        ))

        eventRegistration.registrationList.forEach { registration ->
            fileOutput.add(listOf(
                registration.participantName,
                registration.participantContactNumber ?: "",
                registration.participantAddress ?: "",
                ""
            ))
        }

        val name = Util.generateNameForEventExport(eventName)
        Util.generateExportDir(exportConfig?.exportFilePath!!, name)
        Util.generateRegistrationCsv(exportConfig?.exportFilePath!!, name, fileOutput)

        return File("${exportConfig?.exportFilePath}/$name")
    }

    fun exportPotluckContributionAsCsv(eventName: String, eventPotluck: EventPotluck): File {
        // Potluck contribution output - list
        val fileOutput = mutableListOf<List<String>>()

        // Potluck contribution Columns
        fileOutput.add(listOf(
            "Potluck Item", "Name", "Contact Number", "Status"
        ))

        eventPotluck.potluckItemList.forEach { potluckItem ->
            fileOutput.add(listOf(potluckItem.itemName, "", "", ""))

            potluckItem.contributorList.forEach { contributor ->
                fileOutput.add(listOf("", contributor.contributorName, contributor.contributorContactNumber, ""))
            }
        }

        val name = Util.generateNameForEventExport(eventName)
        Util.generateExportDir(exportConfig?.exportFilePath!!, name)
        Util.generatePotluckCsv(exportConfig?.exportFilePath!!, name, fileOutput)

        return File("${exportConfig?.exportFilePath}/$name")
    }

    fun exportEventSignUpSheetAsCsv(eventName: String, signUpSheet: EventSignUpSheet): File {
        // Sign-up sheet output - list
        val fileOutput = mutableListOf<List<String>>()

        // Sign-up sheet Columns
        fileOutput.add(listOf(
            "Name", "Contact Number", "Status"
        ))

        signUpSheet.contributorList.forEach { contributor ->
            fileOutput.add(listOf(
                contributor.contributorName,
                contributor.contributorContactNumber,
                ""
            ))
        }

        val name = Util.generateNameForEventExport("$eventName-${signUpSheet.sheetName}")
        Util.generateExportDir(exportConfig?.exportFilePath!!, name)
        Util.generateSignUpSheetCsv(exportConfig?.exportFilePath!!, name, fileOutput)

        return File("${exportConfig?.exportFilePath}/$name")
    }
}