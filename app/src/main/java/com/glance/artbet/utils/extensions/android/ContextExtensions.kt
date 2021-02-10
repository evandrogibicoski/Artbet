package com.glance.artbet.utils.extensions.android

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


fun Context.getColorsArray(@ArrayRes colorArrayResource: Int): ArrayList<Int> {
    val colors = resources.obtainTypedArray(colorArrayResource)
    val colorsList = arrayListOf<Int>()
    for (i in 0 until colors.length())
        colorsList.add(colors.getResourceId(i, android.R.color.white))
    colors.recycle()
    return colorsList
}

fun Context.getColorRes(@ColorRes colorResourceId: Int) =
    ContextCompat.getColor(this, colorResourceId)

fun Context.getDefaultBottomBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}

fun Context.calculateStatusBarHeightByResources(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}
