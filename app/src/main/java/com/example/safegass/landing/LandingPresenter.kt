package com.example.safegass.landing

class LandingPresenter(private val model: LandingContract.Model) : LandingContract.Presenter {

    private var view: LandingContract.View? = null

    override fun attachView(view: LandingContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadLandingContent() {
        view?.showAppName(model.getAppName())
        view?.showMainTitle(model.getMainTitle())
        view?.showWelcomeText(model.getWelcomeText())
        view?.showDescription(model.getDescription())
    }

    override fun onGetStartedClicked() {
        view?.navigateToNextScreen()
    }

    override fun onInfoClicked() {
        view?.showInfoDialog()
    }
}
