package com.example.safegass.settings

import android.content.Context
import android.content.SharedPreferences

class SettingsModel(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("SafeGasPrefs", Context.MODE_PRIVATE)

    // --- Language Settings ---
    fun getLanguage(): String {
        return prefs.getString("language", "English") ?: "English"
    }

    fun saveLanguage(language: String) {
        prefs.edit().putString("language", language).apply()
    }

    // --- Sync Time ---
    fun getLastSync(): String {
        return prefs.getString("last_sync", "Last synced: Never") ?: "Last synced: Never"
    }

    fun updateLastSync() {
        prefs.edit().putString("last_sync", "Last synced: Just now").apply()
    }

    // --- Logout / Session ---
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
