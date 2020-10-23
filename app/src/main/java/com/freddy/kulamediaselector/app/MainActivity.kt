package com.freddy.kulamediaselector.app

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.freddy.kulamediaselector.KulaMediaSelectorHelper
import com.freddy.kulamediaselector.config.MediaSelectorOptions
import com.freddy.kulamediaselector.config.MediaType
import com.freddy.kulamediaselector.config.SelectType

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaSelectorOptions = MediaSelectorOptions.Builder()
            .setMediaType(MediaType.ALL)
            .setSelectType(SelectType.MULTIPLE)
            .setTopBarColor(Color.parseColor("#000000"))
            .setBottomBarColor(Color.parseColor("#000000"))
            .setTitleBackgroudColor(Color.parseColor("#999999"))
            .setTitleTextColor(Color.parseColor("#ffffff"))
            .setTitleTextSize(14.0f)
            .setConfirmText("确定")
            .setConfirmTextSize(14.0f)
            .setConfirmTextColor(Color.parseColor("#000000"))
            .setConfirmNormalBackgroundColor(Color.parseColor("#ffde59"))
            .setConfirmPressedBackgroundColor(Color.parseColor("#cd9b1d"))
            .setConfirmDisabledBackgroundColor(Color.parseColor("#999999"))
            .setPreviewText("预览")
            .setPreviewTextSize(14.0f)
            .setPreviewTextColor(Color.parseColor("#000000"))
            .setPreviewNormalBackgroundColor(Color.parseColor("#ffde59"))
            .setPreviewPressedBackgroundColor(Color.parseColor("#cd9b1d"))
            .setPreviewDisabledBackgroundColor(Color.parseColor("#999999"))
            .setCornerMarkerBackgroundColor(Color.parseColor("#ffde59"))
            .setCornerMarkerTextSize(10.0f)
            .setCornerMarkerTextColor(Color.parseColor("#000000"))
            .setSpanCount(4)
            .setMaxSelectCount(9)
            .setMaxVisibilityFolderCount(5)
            .setMaxVideoDuration(10 * 60 * 1000L)
            .setThumbnailWidth(DensityUtil.dip2px(applicationContext, 100.0f))
            .setThumbnailHeight(DensityUtil.dip2px(applicationContext, 100.0f))
            .build()
        KulaMediaSelectorHelper.INSTANCE.goto(this, mediaSelectorOptions)
    }
}