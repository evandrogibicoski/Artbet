package com.glance.artbet.utils.extensions.android.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View


fun View.animateBounce(
    initialValue: Float = 0.9f,
    time: Long = 100,
    onAnimationEnd: () -> Unit = {}
) {
    animate().scaleX(initialValue).scaleY(initialValue).alpha(0.5f).setDuration(time)
        .setListener(getAnimationEndListener {
            animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(time)
                .setListener(getAnimationEndListener { onAnimationEnd() })
        })
}

private fun getAnimationEndListener(onAnimationEnd: () -> Unit = {}): AnimatorListenerAdapter {
    return object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            onAnimationEnd()
        }
    }
}