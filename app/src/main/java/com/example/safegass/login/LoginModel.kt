package com.example.safegass.login

class LoginModel {
    fun authenticate(email: String, password: String): Boolean {
        // Dummy authentication for now
        return email == "user@example.com" && password == "123456"
    }
}
