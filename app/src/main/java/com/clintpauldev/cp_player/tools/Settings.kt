package com.clintpauldev.cp_player.tools

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.clintpauldev.cp_player.tools.Settings.init

object Settings : SingletonHolder<SharedPreferences, Context>({ init(it) }) {

    fun init(context: Context): SharedPreferences {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs
    }

}


const val INITIAL_PERMISSION_ASKED = "initial_permission_asked"

fun SharedPreferences.putSingle(key: String, value: Any) {
    when(value) {
        is Boolean -> edit { putBoolean(key, value) }
        is Int -> edit { putInt(key, value) }
        is Float -> edit { putFloat(key, value) }
        is Long -> edit { putLong(key, value) }
        is String -> edit { putString(key, value) }
        is List<*> -> edit { putStringSet(key, value.toSet() as Set<String>) }
        else -> throw IllegalArgumentException("value class is invalid!")
    }
}