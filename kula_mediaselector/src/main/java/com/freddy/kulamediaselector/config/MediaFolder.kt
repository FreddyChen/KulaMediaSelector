package com.freddy.kulamediaselector.config

import android.net.Uri
import kotlin.properties.Delegates

class MediaFolder : Comparable<MediaFolder> {

    var name: String? = null
    var coverUri: Uri? = null
    var medias: ArrayList<Media> = arrayListOf()
    var sortPriority: Int by Delegates.notNull()

    companion object {
        const val IMAGE_AND_VIDEO_SORT_PRIORITY = 1
        const val ALL_IMAGE_SORT_PRIORITY = 2
        const val ALL_VIDEO_SORT_PRIORITY = 3
        var startPriority = 3
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaFolder

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "MediaFolder(name='$name', coverUri=$coverUri, medias=$medias)"
    }

    override fun compareTo(other: MediaFolder): Int {
        return when {
            sortPriority > other.sortPriority -> 1
            sortPriority == other.sortPriority -> 0
            else -> -1
        }
    }
}