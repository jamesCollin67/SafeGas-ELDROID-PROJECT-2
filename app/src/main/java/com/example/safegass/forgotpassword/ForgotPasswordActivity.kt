package com.example.safegass.forgotpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.safegass.R
import com.example.safegass.login.LoginActivity

class ForgotPasswordActivity : Activity(), ForgotPasswordContract.View {

    private lateinit var inputEmail: EditText
    private lateinit var buttonSendReset: Button
    private lateinit var textBackToLogin: TextView
    private lateinit var backIcon: ImageView

    private lateinit var presenter: ForgotPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        presenter = ForgotPasswordPresenter(this)

        inputEmail = findViewById(R.id.inputEmails)
        buttonSendReset = findViewById(R.id.buttonSendResets)
        textBackToLogin = findViewById(R.id.textBackToLogins)
        backIcon = findViewById(R.id.backIcons)

        buttonSendReset.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            presenter.handleSendReset(email)
        }

        textBackToLogin.setOnClickListener {
            navigateToLogin()
        }

        backIcon.setOnClickListener {
            navigateToLogin()
        }
    }

    override fun showResetSuccess() {
        Toast.makeText(this, "Reset link sent to your email!", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    override fun showResetError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
