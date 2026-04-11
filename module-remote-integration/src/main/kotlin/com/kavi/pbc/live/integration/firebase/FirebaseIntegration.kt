package com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kavi.pbc.live.com.kavi.pbc.live.integration.IntegrationContract
import com.kavi.pbc.live.data.property.IntegrationEnv

class FirebaseIntegration: IntegrationContract {

    private var firebaseApplication: FirebaseApp? = null
    private var appEnv: IntegrationEnv = IntegrationEnv.UNDEFINED

    companion object {
        var shared: FirebaseIntegration = FirebaseIntegration()
    }

    fun getFirebaseApp(): FirebaseApp? {
        return firebaseApplication
    }

    fun isInitiated(): Boolean {
        return firebaseApplication?.let { true }?: run { false }
    }

    override fun init(env: IntegrationEnv) {
        // Assign env
        appEnv = env

        // Initiate firebase core
        FirebaseIntegration::class.java.getResourceAsStream(getEnvFilePath(env)).use {
                inputStream ->
            inputStream?.let {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(it))
                    .setStorageBucket(getStorageBucketName())
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
            IntegrationEnv.UNDEFINED -> "NOT DEFINED"
            IntegrationEnv.STAGING -> "/firebase/pbc-live-service-account-key-staging.json"
            IntegrationEnv.PROD -> "/firebase/pbc-live-service-account-key-prod.json"
        }
    }

    fun getStorageBucketName(): String {
        return when(appEnv) {
            IntegrationEnv.UNDEFINED -> "NOT DEFINED"
            IntegrationEnv.STAGING -> "pbc-live-staging-8bd3e.firebasestorage.app"
            IntegrationEnv.PROD -> "pbc-live-prod.firebasestorage.app"
        }
    }
}