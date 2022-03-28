package com.seth.mawinguweather.util

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun setGlideImage(image: ImageView, url: String) {
        Glide.with(image).load(url)
            .thumbnail(0.5f)
            .into(image)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
        SimpleDateFormat(dateFormat).format(Date())

}