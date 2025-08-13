package com.kavi.pbc.live.api

import com.kavi.pbc.live.com.kavi.pbc.live.datastore.IntegrationEnv
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.firebase.FirebaseIntegration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class AppStartupListener: ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    lateinit var appProperties: AppProperties

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        // Initialize firebase fire-store with environment
        initializeFirebase()
    }

    private fun initializeFirebase() {
        when(appProperties.appEnv) {
            "staging" -> {
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Initiate Firebase")
                FirebaseIntegration.getInstance().init(IntegrationEnv.STAGING)
            }
        }
    }
}