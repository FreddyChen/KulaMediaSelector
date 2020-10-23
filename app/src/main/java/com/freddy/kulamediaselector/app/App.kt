package com.freddy.kulamediaselector.app

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.freddy.kulamediaselector.IImageLoaderEnginee
import com.freddy.kulamediaselector.KulaMediaSelectorHelper

class App : Application() {

    companion object {
        @JvmStatic
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKulaMediaSelector()
    }

    private fun initKulaMediaSelector() {
        KulaMediaSelectorHelper.INSTANCE.init(object : IImageLoaderEnginee {
            override fun loadImage(context: Context, uri: Uri, imageView: ImageView) {
                Glide.with(context).load(uri).into(imageView)
            }

            override fun loadImage(context: Context, bitmap: Bitmap, imageView: ImageView) {
                Glide.with(context).load(bitmap).into(imageView)
            }
        })
    }
}