package com.kavi.pbc.live.com.kavi.pbc.live.datastore.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.DatastoreIntegration
import com.kavi.pbc.live.com.kavi.pbc.live.datastore.IntegrationEnv
import com.kavi.pbc.live.com.kavi.pbc.live.firebase.repository.FirebaseDatastoreRepository
import java.io.ByteArrayInputStream

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
        FirebaseDatastoreRepository::class.java.getResourceAsStream(getEnvFilePath(env)).use {
                inputStream ->
            inputStream?.let {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(it))
                    .build()

                firebaseApplication = FirebaseApp.initializeApp(options)
            }
        }

        /*val credentialsJson = System.getenv("GOOGLE_CREDENTIALS")
            ?: throw IllegalStateException("GOOGLE_CREDENTIALS is not set")

        val serviceAccountStream = ByteArrayInputStream(credentialsJson.toByteArray(Charsets.UTF_8))

        val options = FirebaseOptions.builder()
            .setCredentials(ServiceAccountCredentials.fromStream(serviceAccountStream))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }*/
    }

    /**
     * This returns the correct firebase configuration file path for given environment
     */
    override fun getEnvFilePath(env: IntegrationEnv): String {
        return when(env) {
            IntegrationEnv.STAGING -> "/firebase/pbc-live-service-account-key-staging.json"
            IntegrationEnv.PROD -> "/firebase/pbc-live-service-account-key-prod.json"
        }
    }
}