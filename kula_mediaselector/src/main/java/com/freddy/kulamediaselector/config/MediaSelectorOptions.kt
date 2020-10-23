package com.freddy.kulamediaselector.config

import androidx.annotation.ColorInt
import java.io.Serializable
import kotlin.properties.Delegates

class MediaSelectorOptions(
    var mediaType: MediaType?,
    var selectType: SelectType?,
    var topBarColor: Int?,
    var bottomBarColor: Int?,
    var titleBackgroudColor: Int?,
    var titleTextSize: Float?,
    var titleTextColor: Int?,
    var confirmText: String?,
    var confirmTextSize: Float?,
    var confirmTextColor: Int?,
    var confirmNormalBackgroundColor: Int?,
    var confirmPressedBackgroundColor: Int?,
    var confirmDisabledBackgroundColor: Int?,
    var previewText: String?,
    var previewTextSize: Float?,
    var previewTextColor: Int?,
    var previewNormalBackgroundColor: Int?,
    var previewPressedBackgroundColor: Int?,
    var previewDisabledBackgroundColor: Int?,
    var cornerMarkerBackgroundColor: Int?,
    var cornerMarkerTextSize: Float?,
    var cornerMarkerTextColor: Int?,
    var spanCount: Int,
    var maxSelectCount: Int,
    var maxVisibilityFolderCount: Int,
    var maxVideoDuration: Long,
    var selectedMediaList: ArrayList<Media>?,
    var thumbnailWidth: Int,
    var thumbnailHeight: Int
) : Serializable {

    companion object {
        const val KEY_MEDIA_SELECTOR_OPTIONS = "key_media_selector_options"
    }

    class Builder {
        private var mediaType: MediaType? = null
        private var selectType: SelectType? = null
        private var topBarColor: Int? = null
        private var bottomBarColor: Int? = null
        private var titleBackgroudColor: Int? = null
        private var titleTextSize: Float? = null
        private var titleTextColor: Int? = null
        private var confirmText: String? = null
        private var confirmTextSize: Float? = null
        private var confirmTextColor: Int? = null
        private var confirmNormalBackgroundColor: Int? = null
        private var confirmPressedBackgroundColor: Int? = null
        private var confirmDisabledBackgroundColor: Int? = null
        private var previewText: String? = null
        private var previewTextSize: Float? = null
        private var previewTextColor: Int? = null
        private var previewNormalBackgroundColor: Int? = null
        private var previewPressedBackgroundColor: Int? = null
        private var previewDisabledBackgroundColor: Int? = null
        private var cornerMarkerBackgroundColor: Int? = null
        private var cornerMarkerTextSize: Float? = null
        private var cornerMarkerTextColor: Int? = null
        private var spanCount: Int by Delegates.notNull()
        private var maxSelectCount: Int by Delegates.notNull()
        private var maxVisibilityFolderCount: Int by Delegates.notNull()
        private var maxVideoDuration: Long by Delegates.notNull()
        private var selectedMediaList: ArrayList<Media>? = null
        private var thumbnailWidth: Int by Delegates.notNull()
        private var thumbnailHeight: Int by Delegates.notNull()

        init {
            mediaType = MediaType.ALL
            selectType = SelectType.MULTIPLE
            confirmText = "确定"
            previewText = "预览"
            spanCount = 4
            maxSelectCount = 9
            maxVisibilityFolderCount = 7
            maxVideoDuration = 5 * 60 * 1000L
            thumbnailWidth = 500
            thumbnailHeight = 500
        }

        fun setMediaType(mediaType: MediaType): Builder {
            this.mediaType = mediaType
            return this
        }

        fun setSelectType(selectType: SelectType): Builder {
            this.selectType = selectType
            return this
        }

        fun setTopBarColor(@ColorInt topBarColor: Int): Builder {
            this.topBarColor = topBarColor
            return this
        }

        fun setBottomBarColor(@ColorInt bottomBarColor: Int): Builder {
            this.bottomBarColor = bottomBarColor
            return this
        }

        fun setTitleBackgroudColor(@ColorInt titleBackgroupColor: Int): Builder {
            this.titleBackgroudColor = titleBackgroupColor
            return this
        }

        fun setTitleTextSize(titleTextSize: Float): Builder {
            this.titleTextSize = titleTextSize
            return this
        }

        fun setTitleTextColor(@ColorInt titleTextColor: Int): Builder {
            this.titleTextColor = titleTextColor
            return this
        }

        fun setConfirmText(confirmText: String): Builder {
            this.confirmText = confirmText
            return this
        }

        fun setConfirmTextSize(confirmTextSize: Float): Builder {
            this.confirmTextSize = confirmTextSize
            return this
        }

        fun setConfirmTextColor(@ColorInt confirmTextColor: Int): Builder {
            this.confirmTextColor = confirmTextColor
            return this
        }

        fun setConfirmNormalBackgroundColor(@ColorInt confirmNormalBackgroundColor: Int): Builder {
            this.confirmNormalBackgroundColor = confirmNormalBackgroundColor
            return this
        }

        fun setConfirmPressedBackgroundColor(@ColorInt confirmPressedBackgroundColor: Int): Builder {
            this.confirmPressedBackgroundColor = confirmPressedBackgroundColor
            return this
        }

        fun setConfirmDisabledBackgroundColor(@ColorInt confirmDisabledBackgroundColor: Int): Builder {
            this.confirmDisabledBackgroundColor = confirmDisabledBackgroundColor
            return this
        }

        fun setPreviewText(previewText: String): Builder {
            this.previewText = previewText
            return this
        }

        fun setPreviewTextSize(previewTextSize: Float): Builder {
            this.previewTextSize = previewTextSize
            return this
        }

        fun setPreviewTextColor(@ColorInt previewTextColor: Int): Builder {
            this.previewTextColor = previewTextColor
            return this
        }

        fun setPreviewNormalBackgroundColor(@ColorInt previewNormalBackgroundColor: Int): Builder {
            this.previewNormalBackgroundColor = previewNormalBackgroundColor
            return this
        }

        fun setPreviewPressedBackgroundColor(@ColorInt previewPressedBackgroundColor: Int): Builder {
            this.previewPressedBackgroundColor = previewPressedBackgroundColor
            return this
        }

        fun setPreviewDisabledBackgroundColor(@ColorInt previewDisabledBackgroundColor: Int): Builder {
            this.previewDisabledBackgroundColor = previewDisabledBackgroundColor
            return this
        }

        fun setCornerMarkerBackgroundColor(@ColorInt cornerMarkerBackgroundColor: Int): Builder {
            this.cornerMarkerBackgroundColor = cornerMarkerBackgroundColor
            return this
        }

        fun setCornerMarkerTextSize(cornerMarkerTextSize: Float): Builder {
            this.cornerMarkerTextSize = cornerMarkerTextSize
            return this
        }

        fun setCornerMarkerTextColor(@ColorInt cornerMarkerTextColor: Int): Builder {
            this.cornerMarkerTextColor = cornerMarkerTextColor
            return this
        }

        fun setSpanCount(spanCount: Int): Builder {
            this.spanCount = spanCount
            return this
        }

        fun setMaxSelectCount(maxSelectCount: Int): Builder {
            this.maxSelectCount = maxSelectCount
            return this
        }

        fun setMaxVisibilityFolderCount(maxVisibilityFolderCount: Int): Builder {
            this.maxVisibilityFolderCount = maxVisibilityFolderCount
            return this
        }

        fun setMaxVideoDuration(maxVideoDuration: Long): Builder {
            this.maxVideoDuration = maxVideoDuration
            return this
        }

        fun setSelectedMediaList(selectedMediaList: ArrayList<Media>?): Builder {
            this.selectedMediaList = selectedMediaList
            return this
        }

        fun setThumbnailWidth(thumbnailWidth: Int): Builder {
            this.thumbnailWidth = thumbnailWidth
            return this
        }

        fun setThumbnailHeight(thumbnailHeight: Int): Builder {
            this.thumbnailHeight = thumbnailHeight
            return this
        }

        fun build(): MediaSelectorOptions {
            return MediaSelectorOptions(
                mediaType,
                selectType,
                topBarColor,
                bottomBarColor,
                titleBackgroudColor,
                titleTextSize,
                titleTextColor,
                confirmText,
                confirmTextSize,
                confirmTextColor,
                confirmNormalBackgroundColor,
                confirmPressedBackgroundColor,
                confirmDisabledBackgroundColor,
                previewText,
                previewTextSize,
                previewTextColor,
                previewNormalBackgroundColor,
                previewPressedBackgroundColor,
                previewDisabledBackgroundColor,
                cornerMarkerBackgroundColor,
                cornerMarkerTextSize,
                cornerMarkerTextColor,
                spanCount,
                maxSelectCount,
                maxVisibilityFolderCount,
                maxVideoDuration,
                selectedMediaList,
                thumbnailWidth,
                thumbnailHeight
            )
        }
    }

    override fun toString(): String {
        return "MediaSelectorOptions(mediaType=$mediaType, selectType=$selectType, topBarColor=$topBarColor, bottomBarColor=$bottomBarColor, titleBackgroudColor=$titleBackgroudColor, titleTextSize=$titleTextSize, titleTextColor=$titleTextColor, confirmText=$confirmText, confirmTextSize=$confirmTextSize, confirmTextColor=$confirmTextColor, confirmNormalBackgroundColor=$confirmNormalBackgroundColor, confirmPressedBackgroundColor=$confirmPressedBackgroundColor, confirmDisabledBackgroundColor=$confirmDisabledBackgroundColor, previewText=$previewText, previewTextSize=$previewTextSize, previewTextColor=$previewTextColor, previewNormalBackgroundColor=$previewNormalBackgroundColor, previewPressedBackgroundColor=$previewPressedBackgroundColor, previewDisabledBackgroundColor=$previewDisabledBackgroundColor, cornerMarkerBackgroundColor=$cornerMarkerBackgroundColor, cornerMarkerTextSize=$cornerMarkerTextSize, cornerMarkerTextColor=$cornerMarkerTextColor, spanCount=$spanCount, maxSelectCount=$maxSelectCount, maxVisibilityFolderCount=$maxVisibilityFolderCount, maxVideoDuration=$maxVideoDuration, selectedMediaList=$selectedMediaList, thumbnailWidth=$thumbnailWidth, thumbnailHeight=$thumbnailHeight)"
    }
}