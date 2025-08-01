package com.kavi.pbc.live.data.util

import com.kavi.pbc.live.data.DataConstant.RANDOM_TEXT_LENGTH
import kotlin.random.Random

object DataUtil {
    fun idGenerator(suffix: String): String {
        val randomNum = System.currentTimeMillis() +
                Random.nextInt(0, 10000) + Random.nextInt(0, 1000) +
                Random.nextInt(1,100) + Random.nextInt(1, 10)
        val randomText = getRandomString()
        return "$suffix:$randomText:$randomNum"
    }

    private fun getRandomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z')
        return (1..RANDOM_TEXT_LENGTH).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")
    }
}