package com.glance.artbet.utils.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T> T.toJson(): String =
    Gson().toJson(this)

inline fun <reified T> fromJson(json: String? = null): T? {
    return if (json.isNullOrBlank()) null
    else Gson().fromJson<T>(json, object : TypeToken<T>() {}.type)
}
