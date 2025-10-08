package com.example.safegass.register

class RegisterModel : RegisterContract.Model {
    override fun registerUser(fullName: String, email: String, password: String, callback: (Boolean, String?) -> Unit) {
        // Replace this with real network/db logic.
        // For now we simulate success.
        callback(true, null)
    }
}
