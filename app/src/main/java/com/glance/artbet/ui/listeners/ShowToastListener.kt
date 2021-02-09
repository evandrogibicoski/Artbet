package com.glance.artbet.ui.listeners

interface ShowToastListener {
    fun showToast(message: String)
    companion object {
        val empty = object : ShowToastListener {
            override fun showToast(message: String) {}
        }
    }
}