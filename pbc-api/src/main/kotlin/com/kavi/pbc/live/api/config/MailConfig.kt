package com.kavi.pbc.live.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig {

    @Value($$"${spring.mail.host}") lateinit var host: String
    @Value($$"${spring.mail.port}") var port: Int = 587

    private fun createSender(user: String, pass: String): JavaMailSender {
        val sender = JavaMailSenderImpl()
        sender.host = host
        sender.port = port
        sender.username = user
        sender.password = pass

        val props = sender.javaMailProperties
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        return sender
    }

    @Bean(name = ["broadcastSenderOne"])
    fun broadcastSenderOne(
        @Value($$"${mail.broadcast.one.username}") user: String,
        @Value($$"${mail.broadcast.one.password}") pass: String
    ): JavaMailSender = createSender(user, pass)

    @Bean(name = ["broadcastSenderTwo"])
    fun broadcastSenderTwo(
        @Value($$"${mail.broadcast.two.username}") user: String,
        @Value($$"${mail.broadcast.two.password}") pass: String
    ): JavaMailSender = createSender(user, pass)

    @Bean(name = ["broadcastSenderThree"])
    fun broadcastSenderThree(
        @Value($$"${mail.broadcast.three.username}") user: String,
        @Value($$"${mail.broadcast.three.password}") pass: String
    ): JavaMailSender = createSender(user, pass)

    @Bean(name = ["broadcastSenderFour"])
    fun broadcastSenderFour(
        @Value($$"${mail.broadcast.four.username}") user: String,
        @Value($$"${mail.broadcast.four.password}") pass: String
    ): JavaMailSender = createSender(user, pass)

    @Bean(name = ["broadcastSenderFive"])
    fun broadcastSenderFive(
        @Value($$"${mail.broadcast.five.username}") user: String,
        @Value($$"${mail.broadcast.five.password}") pass: String
    ): JavaMailSender = createSender(user, pass)
}