package com.kavi.pbc.live.com.kavi.pbc.live.integration.cloudinary.cdn

import com.cloudinary.utils.ObjectUtils
import com.kavi.pbc.live.com.kavi.pbc.live.integration.CDNIntegration
import com.kavi.pbc.live.com.kavi.pbc.live.integration.cloudinary.CloudinaryIntegration

class CloudinaryStorage: CDNIntegration {
    override fun uploadFile(
        folder: String,
        fileBytes: ByteArray,
        fileName: String?,
        fileContentType: String?
    ): String {
        val uploadResult = CloudinaryIntegration.getInstance().getCloudinary()
            ?.uploader()
            ?.upload(fileBytes, ObjectUtils.asMap("resource_type", "auto"))
        return uploadResult?.get("url").toString()
    }
}