package com.freddy.kulamediaselector

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

interface IImageLoaderEnginee {
    fun loadImage(context: Context, uri: Uri, imageView: ImageView)
    fun loadImage(context: Context, bitmap: Bitmap, imageView: ImageView)
}