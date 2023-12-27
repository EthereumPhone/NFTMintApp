package org.ethereumphone.nftcreator.moduls

import android.content.Context

object PreferencesHelper {
    private const val PREF_NAME = "my_preferences"

    fun setPreference(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getPreference(context: Context, key: String, defaultValue: String): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}