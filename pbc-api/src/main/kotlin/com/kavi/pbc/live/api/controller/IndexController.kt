package com.kavi.pbc.live.api.controller

import com.kavi.pbc.live.api.AppProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {

    @Autowired
    lateinit var appProperties: AppProperties

    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("appVersion", appProperties.appVersion)
        return "index"
    }
}