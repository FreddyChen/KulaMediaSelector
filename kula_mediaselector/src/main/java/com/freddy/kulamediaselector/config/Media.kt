package com.freddy.kulamediaselector.config

import android.graphics.Bitmap
import android.net.Uri

data class Media(
    var id: Int,
    var displayName: String,
    var fileSize: Long,
    var width: Int,
    var height: Int,
    var mimeType: String,
    var addedTime: Long,
    var duration: Long,
    var thumbnail: Bitmap?,
    var folderName: String,
    var mediaType: MediaType,
    var uri: Uri?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Media

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "Media(id=$id, displayName='$displayName', fileSize=$fileSize, width=$width, height=$height, mimeType='$mimeType', addedTime=$addedTime, duration=$duration, thumbnail=$thumbnail, folderName='$folderName', mediaType=$mediaType, uri=$uri)"
    }
}