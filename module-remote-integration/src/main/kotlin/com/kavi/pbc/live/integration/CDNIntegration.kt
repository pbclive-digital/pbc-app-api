package com.kavi.pbc.live.com.kavi.pbc.live.integration

interface CDNIntegration {
    fun uploadFile(folder: String, fileBytes: ByteArray, fileName: String?, fileContentType: String?): String
}