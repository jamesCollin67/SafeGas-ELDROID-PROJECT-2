package com.example.safegass.update

interface UpdatePasswordContract {

    interface View {
        fun showSuccess(message: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun updatePassword(current: String, newPass: String, confirm: String)
    }

    interface Model {
        fun getCurrentPassword(): String
        fun saveNewPassword(newPassword: String)
    }
}
