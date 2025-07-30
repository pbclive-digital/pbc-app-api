package com.kavi.pbc.live.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.kavi.pbc.live"])
class PbcApiApplication

fun main(args: Array<String>) {
	runApplication<PbcApiApplication>(*args)
}
