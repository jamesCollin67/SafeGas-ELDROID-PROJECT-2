package com.example.safegass.view

class ViewPagePresenter(private val view: ViewPageContract.View) : ViewPageContract.Presenter {

    override fun onBackButtonClicked() {
        // Tell the View to navigate
        view.navigateToDashboard()
    }
}
