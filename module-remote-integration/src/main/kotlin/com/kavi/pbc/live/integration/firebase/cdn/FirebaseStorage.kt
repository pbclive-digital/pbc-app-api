package com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn

import com.google.firebase.cloud.StorageClient
import com.kavi.pbc.live.com.kavi.pbc.live.integration.CDNIntegration
import java.util.UUID

class FirebaseStorage: CDNIntegration {

    companion object {
        private var firebaseStorage: FirebaseStorage? = null
        fun getInstance(): FirebaseStorage {
            return firebaseStorage ?: run {
                firebaseStorage = FirebaseStorage()
                return firebaseStorage as FirebaseStorage
            }
        }
    }

    override fun uploadFile(
        folder: String,
        fileBytes: ByteArray,
        fileName: String?,
        fileContentType: String?
    ): String {
        val fileName = "$folder/${UUID.randomUUID()}-${fileName}"

        StorageClient.getInstance().bucket(FirebaseCDNConstant.STORAGE_BUCKET_NAME)
            .create(fileName, fileBytes, fileContentType)

        return "https://firebasestorage.googleapis.com/v0/b/${StorageClient.getInstance().bucket(FirebaseCDNConstant.STORAGE_BUCKET_NAME).name}/o/$fileName?alt=media"
    }
}