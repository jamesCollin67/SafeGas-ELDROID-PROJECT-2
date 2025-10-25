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
    override fun deactivateAccount() {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val userRef = com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("users").child(uid)

            userRef.child("status").setValue("deactivated")
                .addOnSuccessListener {
                    android.widget.Toast.makeText(appContext, "Account marked as deactivated", android.widget.Toast.LENGTH_SHORT).show()
                    view.showLogoutSuccess()
                }
                .addOnFailureListener { e ->
                    view.showError("Failed to deactivate account: ${e.message}")
                }
        } else {
            view.showError("User not logged in")
        }
    }


}
