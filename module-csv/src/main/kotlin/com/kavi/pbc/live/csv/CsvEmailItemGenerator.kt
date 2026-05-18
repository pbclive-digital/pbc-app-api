package com.kavi.pbc.live.csv

import com.kavi.pbc.live.data.model.email.EmailItem
import java.io.File

class CsvEmailItemGenerator {

    companion object {
        val shared = CsvEmailItemGenerator()
    }

    fun readCsvFile(file: File): MutableList<EmailItem> {
        val emailItemList: MutableList<EmailItem> = mutableListOf()
        val rows: List<List<String>> = Util.readLinesFromGivenCsv(file)

        rows.forEach lit@ { row ->
            val email = row[0].trim()
            val ownerName = row[1].trim()

            if (email == "EMAIL" && ownerName == "OWNER NAME") { // IF THIS IS COLUMN HEADINGS
                return@lit
            } else if (email.isEmpty()) { // IF EMAIL IS EMPTY
                return@lit
            } else {
                if (isValidEmail(email)) {
                    emailItemList.add(EmailItem(email, ownerName))
                }
            }
        }

        return emailItemList
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return emailRegex.matches(email)
    }
}