package com.kavi.pbc.live.data.integration

interface DatastoreIntegration {
    fun init(env: IntegrationEnv)

    fun getEnvFilePath(env: IntegrationEnv): String
}