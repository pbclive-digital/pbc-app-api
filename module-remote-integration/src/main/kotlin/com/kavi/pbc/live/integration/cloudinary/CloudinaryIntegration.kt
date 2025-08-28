package com.kavi.pbc.live.com.kavi.pbc.live.integration.cloudinary

import com.cloudinary.Cloudinary
import com.kavi.pbc.live.com.kavi.pbc.live.integration.IntegrationContract
import com.kavi.pbc.live.data.property.IntegrationEnv

class CloudinaryIntegration: IntegrationContract {

    private var cloudinary: Cloudinary? = null

    companion object {
        private var cloudinaryIntegration: CloudinaryIntegration? = null
        fun getInstance(): CloudinaryIntegration {
            return cloudinaryIntegration ?: run {
                cloudinaryIntegration = CloudinaryIntegration()
                return cloudinaryIntegration as CloudinaryIntegration
            }
        }
    }

    fun getCloudinary(): Cloudinary? = cloudinary

    override fun init(env: IntegrationEnv) {
        cloudinary = Cloudinary(getEnvFilePath(env = env))
    }

    override fun getEnvFilePath(env: IntegrationEnv): String {
        val API_KEY = "159516282966497"
        val API_SECRET = "uk8-27k0O8iLvDYiY99BeU9YBl8"
        val CLOUD_NAME = "kv-staging"
        return when(env) {
            IntegrationEnv.STAGING -> "cloudinary://$API_KEY:$API_SECRET@$CLOUD_NAME"
            IntegrationEnv.PROD -> ""
        }
    }
}