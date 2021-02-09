package com.glance.artbet.ui.listeners

import com.glance.artbet.ui.models.TransitionAnimationStatus

interface FragmentTransitionProgressListener {
    fun onFragmentTransitionProgressChanged(status: TransitionAnimationStatus)
    companion object {
        val empty = object: FragmentTransitionProgressListener {
            override fun onFragmentTransitionProgressChanged(status: TransitionAnimationStatus) {

            }
        }
    }
}