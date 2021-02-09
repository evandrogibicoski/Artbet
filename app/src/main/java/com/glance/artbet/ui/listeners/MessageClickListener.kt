package com.glance.artbet.ui.listeners

import android.view.View
import io.reactivex.disposables.Disposable

interface MessageClickListener {
    fun setNotAnimatedClickListener(
        view: View,
        durationMillis: Long = 400,
        onClick: (view: View) -> Unit
    ): Disposable?

    fun setBounceClickListener(
        view: View,
        durationMillis: Long = 400,
        onClick: (view: View) -> Unit
    ): Disposable?
}