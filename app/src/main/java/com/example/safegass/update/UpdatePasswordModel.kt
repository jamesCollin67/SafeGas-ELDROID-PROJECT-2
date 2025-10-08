package com.example.safegass.update

import android.content.Context
import android.content.SharedPreferences

class UpdatePasswordModel(private val context: Context? = null) : UpdatePasswordContract.Model {

    private val sharedPref: SharedPreferences? =
        context?.getSharedPreferences("SafeGasPrefs", Context.MODE_PRIVATE)

    // For demo purposes, default password is "123456"
    override fun getCurrentPassword(): String {
        return sharedPref?.getString("user_password", "123456") ?: "123456"
    }

    override fun saveNewPassword(newPassword: String) {
        sharedPref?.edit()?.putString("user_password", newPassword)?.apply()
    }
}
