package com.example.safegass.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import com.example.safegass.R
import com.example.safegass.forgotpassword.ForgotPasswordActivity
import com.example.safegass.landing.LandingActivity
import com.example.safegass.register.RegisterActivity

class LoginActivity : Activity(), LoginContract.View {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var textForgotPassword: TextView
    private lateinit var textCreateAccount: TextView
    private lateinit var imageViewTogglePassword: ImageView
    private var isPasswordVisible = false

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        textForgotPassword = findViewById(R.id.textForgotPassword)
        textCreateAccount = findViewById(R.id.textCreateAccount)
        imageViewTogglePassword = findViewById(R.id.imageViewTogglePassword)

        imageViewTogglePassword.setOnClickListener { togglePasswordVisibility() }

        buttonSignIn.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            presenter.createAccount(email, password)
        }

        textForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        textCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            editTextPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageViewTogglePassword.setImageResource(R.drawable.ic_eye)
        } else {
            editTextPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageViewTogglePassword.setImageResource(R.drawable.ic_eye_off)
        }
        isPasswordVisible = !isPasswordVisible
        editTextPassword.setSelection(editTextPassword.text.length)
    }

    override fun showLoginSuccess() {
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LandingActivity::class.java))
        finish()
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getContext(): Context = this
}
