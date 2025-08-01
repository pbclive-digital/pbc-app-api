package com.kavi.pbc.live.com.kavi.pbc.live.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDataRepository
import com.kavi.pbc.live.data.integration.DatastoreIntegration
import com.kavi.pbc.live.data.integration.IntegrationEnv

class FirebaseIntegration: DatastoreIntegration {

    private var firebaseApplication: FirebaseApp? = null

    companion object {
        private var firebaseIntegration: FirebaseIntegration? = null
        fun getInstance(): FirebaseIntegration {
            return firebaseIntegration ?: run {
                firebaseIntegration = FirebaseIntegration()
                return firebaseIntegration as FirebaseIntegration
            }
        }
    }

    fun getFirebaseApp(): FirebaseApp? {
        return firebaseApplication
    }

    fun isInitiated(): Boolean {
        return firebaseApplication?.let { true }?: run { false }
    }

    override fun init(env: IntegrationEnv) {
        println("ENV: $env")
        FirebaseDataRepository::class.java.getResourceAsStream(getEnvFilePath(env)).use {
                inputStream ->
            inputStream?.let {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(it))
                    .build()

                firebaseApplication = FirebaseApp.initializeApp(options)
            }
        }
    }

    /**
     * This returns the correct firebase configuration file path for given environment
     */
    override fun getEnvFilePath(env: IntegrationEnv): String {
        return when(env) {
            IntegrationEnv.QA -> "/firebase/pbc-live-service-account-key-qa.json"
            IntegrationEnv.PROD -> "/firebase/pbc-live-service-account-key-qa.json" //TODO - Create a new key for production while application start to deploy
        }
    }
}