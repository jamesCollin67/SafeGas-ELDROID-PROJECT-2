package com.example.safegass.view

interface ViewPageContract {
    interface View {
        fun navigateToDashboard()
    }

    interface Presenter {
        fun onBackButtonClicked()
    }
}
