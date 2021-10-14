package com.imanfz.simpleweather.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.imanfz.simpleweather.BuildConfig.ICON_URL
import com.imanfz.simpleweather.R
import java.text.SimpleDateFormat
import java.util.*

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun ImageView.loadImageFromUrl(id: String){
    if (id.isEmpty()) return

    val imageUrl = "${ICON_URL}${id}.png"
    Glide.with(context)
        .load(imageUrl)
        .apply(
            RequestOptions
                .centerCropTransform()
                .error(R.drawable.ic_broken_image)
        ).into(this)
}

fun Int.getDayWithMonth(): String {
    val nowTime = Calendar.getInstance()
    nowTime.timeInMillis = this * 1000L

    return "Today ${SimpleDateFormat("dd, MMM yy", Locale.getDefault()).format(nowTime.time)}"
}

fun Int.getMonthYear(): String {
    val nowTime = Calendar.getInstance()
    nowTime.timeInMillis = this * 1000L

    return SimpleDateFormat("MMM yy", Locale.getDefault()).format(nowTime.time)
}

fun Int.getDay(): String {
    val nowTime = Calendar.getInstance()
    val valTime = Calendar.getInstance()
    valTime.timeInMillis = (this * 1000L)
    val simpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    return if (valTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1 &&
            valTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH) &&
            valTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR)) {
        "Tomorrow"
    } else {
        simpleDateFormat.format(valTime.time)
    }
}

fun Double.toCelsius(): String {
    return (this - 273.15).toString().take(4) + "\u2103"
}