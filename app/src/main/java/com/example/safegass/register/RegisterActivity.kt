package com.example.safegass.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import com.example.safegass.R
import com.example.safegass.login.LoginActivity

class RegisterActivity : Activity(), RegisterContract.View {

    private lateinit var presenter: RegisterContract.Presenter
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this)

        val etFullName = findViewById<EditText>(R.id.editTextFullName)
        val etEmail = findViewById<EditText>(R.id.editTextEmail)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)
        val etConfirm = findViewById<EditText>(R.id.editTextConfirmPassword)
        val cbTerms = findViewById<CheckBox>(R.id.checkBoxTerms)
        val btnCreate = findViewById<Button>(R.id.buttonCreateAccount)
        val tvSignIn = findViewById<TextView>(R.id.textAlreadyHaveAccount)
        val ivTogglePassword = findViewById<ImageView>(R.id.imageViewTogglePassword)
        val ivToggleConfirm = findViewById<ImageView>(R.id.imageViewToggleConfirmPassword)

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            etPassword.transformationMethod =
                if (isPasswordVisible) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
            etPassword.setSelection(etPassword.text.length)
            ivTogglePassword.setImageResource(if (isPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye)
        }

        ivToggleConfirm.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            etConfirm.transformationMethod =
                if (isConfirmPasswordVisible) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
            etConfirm.setSelection(etConfirm.text.length)
            ivToggleConfirm.setImageResource(if (isConfirmPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye)
        }

        btnCreate.setOnClickListener {
            presenter.onRegisterClicked(
                etFullName.text.toString().trim(),
                etEmail.text.toString().trim(),
                etPassword.text.toString(),
                etConfirm.text.toString(),
                cbTerms.isChecked
            )
        }

        tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun showRegistrationSuccess() {
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun showRegistrationError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
