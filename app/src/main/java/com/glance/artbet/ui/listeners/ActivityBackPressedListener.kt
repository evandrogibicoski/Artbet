package com.glance.artbet.ui.listeners

import androidx.fragment.app.Fragment

interface ActivityBackPressedListener {
    fun onBackPressed(fragment: Fragment)
    companion object {
        val empty = object: ActivityBackPressedListener {
            override fun onBackPressed(fragment: Fragment) {}
        }
    }
}