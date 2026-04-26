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

            if (email == "EMAIL" && ownerName == "OWNER NAME") {
                return@lit
            } else {
                emailItemList.add(EmailItem(email, ownerName))
            }
        }

        return emailItemList
    }
}