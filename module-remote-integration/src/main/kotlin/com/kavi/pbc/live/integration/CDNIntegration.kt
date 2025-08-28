package com.kavi.pbc.live.com.kavi.pbc.live.integration

interface CDNIntegration {
    fun uploadFile(fileBytes: ByteArray, fileName: String, fileContentType: String?): String
}