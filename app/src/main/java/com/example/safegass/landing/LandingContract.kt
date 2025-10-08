package com.example.safegass.landing

interface LandingContract {

    interface View {
        fun showAppName(name: String)
        fun showMainTitle(title: String)
        fun showWelcomeText(text: String)
        fun showDescription(description: String)
        fun navigateToNextScreen()
        fun showInfoDialog()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadLandingContent()
        fun onGetStartedClicked()
        fun onInfoClicked()
    }

    interface Model {
        fun getAppName(): String
        fun getMainTitle(): String
        fun getWelcomeText(): String
        fun getDescription(): String
    }
}
