package com.example.safegass.landing

class LandingModel : LandingContract.Model {
    override fun getAppName(): String = "SafeGass"
    override fun getMainTitle(): String = "Your Safety, Our Priority"
    override fun getWelcomeText(): String = "Welcome to SafeGass!"
    override fun getDescription(): String =
        "Monitor, detect, and respond to gas leaks efficiently to keep your home and family safe."
}
