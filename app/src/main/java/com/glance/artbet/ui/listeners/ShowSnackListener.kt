package com.glance.artbet.ui.listeners

import android.view.View
import androidx.annotation.ColorRes
import com.androidadvance.topsnackbar.TSnackbar
import com.glance.artbet.R

interface ShowSnackListener {
    fun showSnack(
        attachView: View?,
        message: String,
        @ColorRes bgColorResId: Int,
        @ColorRes textColorResId: Int = R.color.colorWhite,
        length: Int = TSnackbar.LENGTH_LONG
    )

    companion object {
        val empty = object : ShowSnackListener {
            override fun showSnack(
                attachView: View?,
                message: String,
                bgColorResId: Int,
                textColorResId: Int,
                length: Int
            ) {

            }
        }
    }
}