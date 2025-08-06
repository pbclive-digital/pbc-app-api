package com.kavi.pbc.live.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

// Annotate this to start this multi-module-gradle-project
@SpringBootApplication(scanBasePackages = ["com.kavi.pbc.live"])
@EnableScheduling
class PbcApiApplication

fun main(args: Array<String>) {
	runApplication<PbcApiApplication>(*args)
}
