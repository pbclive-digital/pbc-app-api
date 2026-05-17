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

    @Bean(name = ["eventNotifier"])
    fun eventEmailNotifier(
        @Value($$"${mail.event.username}") user: String,
        @Value($$"${mail.event.password}") pass: String
    ): JavaMailSender = createSender(user, pass)

    @Bean(name = ["infoNotifier"])
    fun infoEmailNotifier(
        @Value($$"${mail.info.username}") user: String,
        @Value($$"${mail.info.password}") pass: String
    ): JavaMailSender = createSender(user, pass)

    @Bean(name = ["broadcastNotifier"])
    fun broadcastEmailNotifier(
        @Value($$"${mail.broadcast.username}") user: String,
        @Value($$"${mail.broadcast.password}") pass: String
    ): JavaMailSender = createSender(user, pass)
}