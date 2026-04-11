package com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.cdn

import com.google.cloud.storage.BlobInfo
import com.google.firebase.cloud.StorageClient
import com.kavi.pbc.live.com.kavi.pbc.live.integration.CDNIntegration
import com.kavi.pbc.live.com.kavi.pbc.live.integration.firebase.FirebaseIntegration
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
        fileBytes: ByteArray,
        fileName: String,
        fileContentType: String?
    ): String {
        val token = UUID.randomUUID().toString()

        val bucket = StorageClient.getInstance().bucket(FirebaseIntegration.shared.getStorageBucketName())

        val blobInfo = BlobInfo.newBuilder(bucket.name, fileName)
            .setContentType(fileContentType)
            .setMetadata(mapOf("firebaseStorageDownloadTokens" to token))
            .build()

        bucket.storage.create(blobInfo, fileBytes)

        return "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/${fileName.replace("/", "%2F")}?alt=media&token=$token"
    }
}