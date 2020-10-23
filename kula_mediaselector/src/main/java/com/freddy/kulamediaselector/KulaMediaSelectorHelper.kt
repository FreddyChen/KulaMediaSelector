package com.freddy.kulamediaselector

import android.content.Context
import android.content.Intent
import com.freddy.kulamediaselector.config.MediaSelectorOptions
import com.freddy.kulamediaselector.ui.AlbumActivity

class KulaMediaSelectorHelper private constructor() {

    var imageLoaderEnginee: IImageLoaderEnginee? = null
    companion object {
        val INSTANCE: KulaMediaSelectorHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            KulaMediaSelectorHelper()
        }
    }

    fun init(imageLoaderEnginee: IImageLoaderEnginee?) {
        this.imageLoaderEnginee = imageLoaderEnginee
    }

    fun goto(context: Context, options: MediaSelectorOptions) {
        val intent = Intent(context, AlbumActivity::class.java)
        intent.putExtra(MediaSelectorOptions.KEY_MEDIA_SELECTOR_OPTIONS, options)
        context.startActivity(intent)
    }
}