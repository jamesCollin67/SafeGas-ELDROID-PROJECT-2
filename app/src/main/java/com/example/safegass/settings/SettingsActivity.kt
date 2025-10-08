package com.example.safegass.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.safegass.R
import com.example.safegass.dashboard.DashboardActivity

class SettingsActivity : Activity(), SettingsContract.View {

    private lateinit var presenter: SettingsPresenter

    private lateinit var inputLanguage: EditText
    private lateinit var textLastSync: TextView
    private lateinit var btnApplyLanguage: Button
    private lateinit var btnManualSync: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        presenter = SettingsPresenter(this, this)

        inputLanguage = findViewById(R.id.inputLanguage)
        textLastSync = findViewById(R.id.textLastSync)
        btnApplyLanguage = findViewById(R.id.btnApplyLanguage)
        btnManualSync = findViewById(R.id.btnManualSync)
        btnLogout = findViewById(R.id.btnLogout)

        // Load initial data
        presenter.loadSettings()

        // Button actions
        btnApplyLanguage.setOnClickListener {
            presenter.applyLanguage(inputLanguage.text.toString())
        }

        btnManualSync.setOnClickListener {
            presenter.manualSync()
        }

        btnLogout.setOnClickListener {
            presenter.logout()
        }
    }

    // === MVP Methods ===
    override fun showLastSync(time: String) {
        textLastSync.text = time
    }

    override fun showLanguage(language: String) {
        inputLanguage.setText(language)
    }

    override fun showLogoutSuccess() {
        // Return to login or dashboard after logout
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun showError(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
