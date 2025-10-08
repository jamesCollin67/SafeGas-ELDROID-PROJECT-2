package com.example.safegass.register

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val model: RegisterContract.Model = RegisterModel()
) : RegisterContract.Presenter {

    override fun onRegisterClicked(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        agreedToTerms: Boolean
    ) {
        // Basic validation on presenter level
        when {
            fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                view.showRegistrationError("Please fill in all required fields.")
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                view.showRegistrationError("Invalid email format.")
                return
            }
            password.length < 6 -> {
                view.showRegistrationError("Password must be at least 6 characters.")
                return
            }
            password != confirmPassword -> {
                view.showRegistrationError("Passwords do not match.")
                return
            }
            !agreedToTerms -> {
                view.showRegistrationError("Please agree to the Terms & Privacy Policy.")
                return
            }
            else -> {
                // Delegate to model
                model.registerUser(fullName, email, password) { success, error ->
                    if (success) view.showRegistrationSuccess()
                    else view.showRegistrationError(error ?: "Registration failed.")
                }
            }
        }
    }
}
