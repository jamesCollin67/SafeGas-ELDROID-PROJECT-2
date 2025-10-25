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
import com.google.firebase.auth.FirebaseAuth

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

            // ✅ Validation
            if (email.isEmpty()) {
                editTextEmail.error = "Email is required"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = "Enter a valid email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                editTextPassword.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < 6) {
                editTextPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }


            presenter.handleLogin(email, password)
        }

        textForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        textCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        val resendButton = findViewById<Button>(R.id.btnResendVerification)
        resendButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            if (user != null && !user.isEmailVerified) {
                user.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Verification email sent!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to resend email.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please log in first to resend verification.", Toast.LENGTH_SHORT).show()
            }
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
    fun showResendButton() {
        val resendButton = findViewById<Button>(R.id.btnResendVerification)
        resendButton.visibility = Button.VISIBLE
    }


    override fun getContext(): Context = this
}
