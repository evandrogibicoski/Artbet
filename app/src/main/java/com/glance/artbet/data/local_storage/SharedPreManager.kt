package com.glance.artbet.data.local_storage

import android.content.Context
import android.content.SharedPreferences
import com.glance.artbet.ArtbetApp

object SharedPreManager {
    private const val COMMON = "Common"

    private val context: Context = ArtbetApp.context

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(COMMON, Context.MODE_PRIVATE)

    fun saveVariable(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun saveVariable(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun saveVariable(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getVariable(key: String, default: String): String =
            sharedPreferences.getString(key, default).toString()

    fun getVariable(key: String, default: Int): Int =
            sharedPreferences.getInt(key, default)

    fun getVariable(key: String, default: Boolean): Boolean =
            sharedPreferences.getBoolean(key, default)
}