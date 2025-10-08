package com.example.safegass.update

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.safegass.R
import com.example.safegass.settings.SettingsActivity

class UpdatePasswordActivity : Activity(), UpdatePasswordContract.View {

    private lateinit var presenter: UpdatePasswordContract.Presenter

    private lateinit var inputCurrentPassword: EditText
    private lateinit var inputNewPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var btnCancel: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        presenter = UpdatePasswordPresenter(this)

        // Bind views
        inputCurrentPassword = findViewById(R.id.inputCurrentPassword)
        inputNewPassword = findViewById(R.id.inputNewPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        btnCancel = findViewById(R.id.btnCancel)
        btnBack = findViewById(R.id.btnBack)

        // Save button listener
        btnSaveChanges.setOnClickListener {
            val current = inputCurrentPassword.text.toString()
            val newPass = inputNewPassword.text.toString()
            val confirm = inputConfirmPassword.text.toString()
            presenter.updatePassword(current, newPass, confirm)
        }

        // Cancel button → go back to Settings
        btnCancel.setOnClickListener {
            navigateBack()
        }

        // Arrow back → also go back to Settings
        btnBack.setOnClickListener {
            navigateBack()
        }
    }

    private fun navigateBack() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    // === View Implementations ===
    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        navigateBack()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
