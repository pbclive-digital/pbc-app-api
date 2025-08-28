package com.kavi.pbc.live.com.kavi.pbc.live.integration

import com.kavi.pbc.live.data.property.IntegrationEnv

interface IntegrationContract {
    fun init(env: IntegrationEnv)
    fun getEnvFilePath(env: IntegrationEnv): String
}