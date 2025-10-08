package com.example.safegass.login

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    private val model = LoginModel()

    override fun handleLogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showLoginError("Please enter both email and password")
        } else if (model.authenticate(email, password)) {
            view.showLoginSuccess()
        } else {
            view.showLoginError("Invalid credentials")
        }
    }
}
