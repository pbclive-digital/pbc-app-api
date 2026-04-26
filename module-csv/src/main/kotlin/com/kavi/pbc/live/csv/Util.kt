package com.kavi.pbc.live.csv

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object Util {
    private const val REGISTRATION_FILE = "event-registration.csv"
    private const val POTLUCK_CONTRIBUTION_FILE = "potluck-contribution.csv"
    private const val SIGN_UP_SHEET_CONTRIBUTION_FILE = "sign-up-sheet-contribution.csv"

    /**
     * Generate directory to store export files
     */
    fun generateExportDir(exportPath: String, name: String) {
        val path = "${exportPath}/${name}"
        val exportDir = File(path)
        if (!exportDir.isDirectory) {
            try {
                Files.createDirectories(Paths.get(path)).toFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Generate name for export file's directory
     */
    fun generateNameForEventExport(eventName: String): String {
        val renameName = eventName.trim().replace(" ", "_").lowercase()
        return "${renameName}_${getCurrentTimestamp()}"
    }

    /**
     * Generate registration csv document
     */
    fun generateRegistrationCsv(exportPath: String, name: String, singleOutputList: List<List<String>>): File {
        csvWriter().open("${exportPath}/${name}/$REGISTRATION_FILE") {
            writeRows(singleOutputList)
        }

        return File("$exportPath/$name/$REGISTRATION_FILE")
    }

    /**
     * Generate potluck contribution csv document
     */
    fun generatePotluckCsv(exportPath: String, name: String, singleOutputList: List<List<String>>): File {
        csvWriter().open("${exportPath}/${name}/$POTLUCK_CONTRIBUTION_FILE") {
            writeRows(singleOutputList)
        }

        return File("$exportPath/$name/$POTLUCK_CONTRIBUTION_FILE")
    }

    /**
     * Generate potluck contribution csv document
     */
    fun generateSignUpSheetCsv(exportPath: String, name: String, singleOutputList: List<List<String>>): File {
        csvWriter().open("${exportPath}/${name}/$SIGN_UP_SHEET_CONTRIBUTION_FILE") {
            writeRows(singleOutputList)
        }

        return File("$exportPath/$name/$SIGN_UP_SHEET_CONTRIBUTION_FILE")
    }

    /**
     * Read lines of given csv document.
     */
    fun readLinesFromGivenCsv(file: File): List<List<String>> = csvReader().readAll(file)

    private fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}