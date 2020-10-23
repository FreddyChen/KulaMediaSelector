package com.freddy.kulamediaselector.media

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Size
import com.freddy.kulamediaselector.config.Media
import com.freddy.kulamediaselector.config.MediaFolder
import com.freddy.kulamediaselector.config.MediaSelectorOptions
import com.freddy.kulamediaselector.config.MediaType
import com.freddy.kulamediaselector.listener.OnMediaScannerListener
import java.io.File
import kotlin.Comparator

class MediaScanner(private val context: Context, private val mediaSelectorOptions: MediaSelectorOptions) {

    private var imageProjection: Array<String>
    private var videoProjection: Array<String>
    private val mediaFolderList = arrayListOf<MediaFolder>()
    private var imageAndVideoMediaFolder: MediaFolder? = null
    private var allImageMediaFolder: MediaFolder? = null
    private var allVideoMediaFolder: MediaFolder? = null

    companion object {
        private const val TAG = "MediaScanner"
    }

    init {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                imageProjection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.DATE_ADDED
                )

                videoProjection = arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.WIDTH,
                    MediaStore.Video.Media.HEIGHT,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.DATE_ADDED
                )
            }

            else -> {
                imageProjection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.DATA
                )

                videoProjection = arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.WIDTH,
                    MediaStore.Video.Media.HEIGHT,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.DATA
                )
            }
        }
    }

    /**
     * 扫描媒体文件
     */
    fun scanMedias(mediaType: MediaType, listener: OnMediaScannerListener?) {
        when (mediaType) {
            MediaType.IMAGE -> {
                scanImages()
            }

            MediaType.VIDEO -> {
                scanVideos()
            }

            MediaType.ALL -> {
                scanImages()
                scanVideos()
            }
        }

        for (mediaFolder in mediaFolderList) {
            val medias = mediaFolder.medias
            if (medias.isNotEmpty()) {
                medias.sortWith(Comparator { p0, p1 ->
                    when {
                        p0.addedTime > p1.addedTime -> -1
                        p0.addedTime == p1.addedTime -> 0
                        else -> 1
                    }
                })
            }
        }
        mediaFolderList.sort()
        listener?.onResultMediaFolders(mediaFolderList)
    }

    private fun scanImages() {
        val selection = StringBuilder()
            .append(MediaStore.Images.Media.MIME_TYPE)
            .append("=? or ")
            .append(MediaStore.Images.Media.MIME_TYPE)
            .append("=? or ")
            .append(MediaStore.Images.Media.MIME_TYPE)
            .append("=?")
            .toString()
        val selectionArgs = arrayOf("image/jpeg", "image/png", "image/webp")
        Log.d(TAG, "selection = $selection, selectionArgs = $selectionArgs")
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            selection,
            selectionArgs,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val displayName =
                    getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val fileSize = getLong(getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                val width = getInt(getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH))
                val height = getInt(getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT))
                val mimeType = getString(getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                val addedTime = getLong(getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))
                var folderName = ""
                val path: String
                val uri: Uri
                var thumbnail: Bitmap? = null
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
                            .appendPath(id.toString()).build().toString()
                        folderName =
                            getString(getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                        uri = Uri.parse(path)
                        thumbnail = context.contentResolver.loadThumbnail(uri, Size(mediaSelectorOptions.thumbnailWidth, mediaSelectorOptions.thumbnailHeight), null)
                    }

                    else -> {
                        path = getString(getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                        if (!TextUtils.isEmpty(path)) {
                            val parentFile = File(path).parentFile
                            parentFile?.let {
                                folderName = it.path
                            }
                        }
                        uri = Uri.parse(path)
                    }
                }

                val media = Media(
                    id,
                    displayName,
                    fileSize,
                    width,
                    height,
                    mimeType,
                    addedTime,
                    0L,
                    thumbnail,
                    folderName,
                    MediaType.IMAGE,
                    uri
                )
                getMediaFolder(folderName, uri).medias.add(media)
                if(imageAndVideoMediaFolder == null) {
                    imageAndVideoMediaFolder = MediaFolder()
                    imageAndVideoMediaFolder!!.name = "图片和视频"
                    imageAndVideoMediaFolder!!.coverUri = uri
                    imageAndVideoMediaFolder!!.sortPriority = MediaFolder.IMAGE_AND_VIDEO_SORT_PRIORITY
                    mediaFolderList.add(imageAndVideoMediaFolder!!)
                }
                imageAndVideoMediaFolder?.medias?.add(media)

                if(allImageMediaFolder == null) {
                    allImageMediaFolder = MediaFolder()
                    allImageMediaFolder!!.name = "所有图片"
                    allImageMediaFolder!!.coverUri = uri
                    allImageMediaFolder!!.sortPriority = MediaFolder.ALL_IMAGE_SORT_PRIORITY
                    mediaFolderList.add(allImageMediaFolder!!)
                }
                allImageMediaFolder?.medias?.add(media)
            }

            close()
        }
    }

    private fun scanVideos() {
        val selection = StringBuilder()
            .append(MediaStore.Video.Media.MIME_TYPE)
            .append("=? and ")
            .append(MediaStore.Video.Media.DURATION)
            .append("<= ${mediaSelectorOptions.maxVideoDuration}")
            .toString()
        val selectionArgs = arrayOf("video/mp4")
        Log.d(TAG, "selection = $selection, selectionArgs = $selectionArgs")
        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            selection,
            selectionArgs,
            MediaStore.Video.Media.DATE_ADDED + " DESC"
        )
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                val displayName =
                    getString(getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val fileSize = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                val width = getInt(getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH))
                val height = getInt(getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
                val mimeType = getString(getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))
                val addedTime = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
                val duration = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                var folderName = ""
                val path: String
                val uri: Uri
                var thumbnail: Bitmap? = null
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        path = MediaStore.Video.Media.EXTERNAL_CONTENT_URI.buildUpon()
                            .appendPath(id.toString()).build().toString()
                        folderName =
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        uri = Uri.parse(path)
                        thumbnail = context.contentResolver.loadThumbnail(uri, Size(mediaSelectorOptions.thumbnailWidth, mediaSelectorOptions.thumbnailHeight), null)
                    }

                    else -> {
                        path = getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                        if (!TextUtils.isEmpty(path)) {
                            val parentFile = File(path).parentFile
                            parentFile?.let {
                                folderName = it.path
                            }
                        }
                        uri = Uri.parse(path)
                    }
                }

                val media = Media(
                    id,
                    displayName,
                    fileSize,
                    width,
                    height,
                    mimeType,
                    addedTime,
                    duration,
                    thumbnail,
                    folderName,
                    MediaType.VIDEO,
                    uri
                )
                getMediaFolder(folderName, uri).medias.add(media)
                if(imageAndVideoMediaFolder == null) {
                    imageAndVideoMediaFolder = MediaFolder()
                    imageAndVideoMediaFolder!!.name = "图片和视频"
                    imageAndVideoMediaFolder!!.coverUri = uri
                    imageAndVideoMediaFolder!!.sortPriority = MediaFolder.IMAGE_AND_VIDEO_SORT_PRIORITY
                    mediaFolderList.add(imageAndVideoMediaFolder!!)
                }
                imageAndVideoMediaFolder?.medias?.add(media)

                if(allVideoMediaFolder == null) {
                    allVideoMediaFolder = MediaFolder()
                    allVideoMediaFolder!!.name = "所有视频"
                    allVideoMediaFolder!!.coverUri = uri
                    allVideoMediaFolder!!.sortPriority = MediaFolder.ALL_VIDEO_SORT_PRIORITY
                    mediaFolderList.add(allVideoMediaFolder!!)
                }
                allVideoMediaFolder?.medias?.add(media)
            }

            close()
        }
    }

    private fun getMediaFolder(folderName: String?, coverUri: Uri): MediaFolder {
        for (folder in mediaFolderList) {
            if (folder.name == folderName) {
                return folder
            }
        }

        val mediaFolder = MediaFolder()
        mediaFolder.name = folderName
        mediaFolder.coverUri = coverUri
        mediaFolder.sortPriority = ++MediaFolder.startPriority
        mediaFolderList.add(mediaFolder)
        return mediaFolder
    }
}