package com.example.safegass.register

import android.content.Context

class RegisterModel(private val context: Context) : RegisterContract.Model {

    override fun registerUser(
        fullName: String,
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val prefs = context.getSharedPreferences("users", Context.MODE_PRIVATE)

        // Check if email already exists
        if (prefs.contains(email)) {
            callback(false, "User already exists with this email.")
            return
        }

        // Save email and password
        prefs.edit().putString(email, password).apply()

        // You could also save fullName if needed
        prefs.edit().putString("${email}_fullname", fullName).apply()

        callback(true, null)
    }
}
