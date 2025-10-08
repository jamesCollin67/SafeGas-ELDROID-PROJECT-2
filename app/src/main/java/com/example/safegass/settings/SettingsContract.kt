package com.example.safegass.settings

interface SettingsContract {
    interface View {
        fun showLastSync(time: String)
        fun showLanguage(language: String)
        fun showLogoutSuccess()
        fun showError(message: String)
    }

    interface Presenter {
        fun loadSettings()
        fun applyLanguage(language: String)
        fun manualSync()
        fun logout()
    }
}
