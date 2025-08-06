package com.kavi.pbc.live.data.util

import com.kavi.pbc.live.data.DataConstant.RANDOM_TEXT_LENGTH
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.random.Random

object DataUtil {
    fun idGenerator(suffix: String): String {
        val randomNum = System.currentTimeMillis() +
                Random.nextInt(0, 10000) + Random.nextInt(0, 1000) +
                Random.nextInt(1,100) + Random.nextInt(1, 10)
        val randomText = getRandomString()
        return "$suffix:$randomText:$randomNum"
    }

    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    fun getOlderTimestamp(timeAgo: Int, unit: ChronoUnit): Long {
        val olderTimeStamp: Long = when(unit) {
            ChronoUnit.SECONDS -> {
                System.currentTimeMillis() - (timeAgo * 1000)
            }
            ChronoUnit.MINUTES -> {
                System.currentTimeMillis() - (timeAgo * 60 * 1000)
            }
            ChronoUnit.HOURS -> {
                System.currentTimeMillis() - (timeAgo * 60 *60 * 1000)
            }
            else -> {
                val currentDate = LocalDate.now()
                val timeAgoStamp = currentDate.minus(timeAgo.toLong(), unit)
                timeAgoStamp.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
            }
        }

        return olderTimeStamp
    }

    private fun getRandomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z')
        return (1..RANDOM_TEXT_LENGTH).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")
    }
}