package com.example.safegass.update

import android.app.Activity
import android.app.AlertDialog
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

        presenter = UpdatePasswordPresenter(this, this)

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
        btnCancel.setOnClickListener { navigateBack() }
        btnBack.setOnClickListener { navigateBack() }
    }

    private fun navigateBack() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    // === 🔹 Modern Popup Dialogs ===
    override fun showSuccess(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                navigateBack()
            }
            .show()
    }

    override fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
