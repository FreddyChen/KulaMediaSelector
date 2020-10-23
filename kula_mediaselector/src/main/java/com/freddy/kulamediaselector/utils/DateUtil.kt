package com.freddy.kulamediaselector.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object DateUtil {

    const val TIME_MM_SS = "mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun formatDate(time: Long, format: String): String? {
        try {
            val sdf = SimpleDateFormat(format)
            return sdf.format(time)
        }catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}