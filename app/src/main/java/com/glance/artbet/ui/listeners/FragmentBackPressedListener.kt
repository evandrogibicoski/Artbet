package com.glance.artbet.ui.listeners

interface FragmentBackPressedListener {
    fun onBackPressed()
    fun shouldExit(): Boolean
}