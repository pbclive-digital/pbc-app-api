package com.kavi.pbc.live.com.kavi.pbc.live.datastore

interface DatastoreIntegration {
    fun init(env: IntegrationEnv)
    fun getEnvFilePath(env: IntegrationEnv): String
}