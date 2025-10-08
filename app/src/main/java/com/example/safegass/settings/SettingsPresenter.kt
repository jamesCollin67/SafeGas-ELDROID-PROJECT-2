package com.example.safegass.settings

import android.content.Context
import android.widget.Toast

class SettingsPresenter(
    private val view: SettingsContract.View,
    context: Context
) : SettingsContract.Presenter {

    private val model = SettingsModel(context)
    private val appContext = context

    override fun loadSettings() {
        val language = model.getLanguage()
        val lastSync = model.getLastSync()

        view.showLanguage(language)
        view.showLastSync(lastSync)
    }

    override fun applyLanguage(language: String) {
        if (language.isBlank()) {
            view.showError("Please enter a valid language.")
            return
        }
        model.saveLanguage(language)
        Toast.makeText(appContext, "Language set to $language", Toast.LENGTH_SHORT).show()
        view.showLanguage(language)
    }

    override fun manualSync() {
        model.updateLastSync()
        Toast.makeText(appContext, "Data synced successfully", Toast.LENGTH_SHORT).show()
        view.showLastSync(model.getLastSync())
    }

    override fun logout() {
        model.clearSession()
        Toast.makeText(appContext, "Logged out successfully", Toast.LENGTH_SHORT).show()
        view.showLogoutSuccess()
    }
}
