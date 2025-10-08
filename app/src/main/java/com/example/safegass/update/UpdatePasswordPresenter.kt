package com.example.safegass.update

class UpdatePasswordPresenter(private val view: UpdatePasswordContract.View) :
    UpdatePasswordContract.Presenter {

    private val model = UpdatePasswordModel()

    override fun updatePassword(current: String, newPass: String, confirm: String) {
        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            view.showError("Please fill in all fields.")
            return
        }

        val savedPassword = model.getCurrentPassword()
        if (current != savedPassword) {
            view.showError("Current password is incorrect.")
            return
        }

        if (newPass.length < 6) {
            view.showError("New password must be at least 6 characters.")
            return
        }

        if (newPass != confirm) {
            view.showError("New passwords do not match.")
            return
        }

        model.saveNewPassword(newPass)
        view.showSuccess("Password updated successfully.")
    }
}
