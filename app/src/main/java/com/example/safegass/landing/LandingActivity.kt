package com.example.safegass.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.safegass.R
import com.example.safegass.dashboard.DashboardActivity

class LandingActivity : Activity(), LandingContract.View {

    private lateinit var presenter: LandingContract.Presenter

    private lateinit var textAppName: TextView
    private lateinit var textMainTitle: TextView
    private lateinit var textWelcome: TextView
    private lateinit var textDescription: TextView
    private lateinit var buttonGetStarted: Button
    private lateinit var iconInfo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // Initialize views
        textAppName = findViewById(R.id.textAppName)
        textMainTitle = findViewById(R.id.textMainTitle)
        textWelcome = findViewById(R.id.textWelcome)
        textDescription = findViewById(R.id.textDescription)
        buttonGetStarted = findViewById(R.id.buttonGetStarted)
        iconInfo = findViewById(R.id.iconInfo)

        // Initialize presenter with model
        presenter = LandingPresenter(LandingModel())
        presenter.attachView(this)

        // Set initial UI text
        presenter.loadLandingContent()

        // Button click event
        buttonGetStarted.setOnClickListener {
            presenter.onGetStartedClicked()
        }

        // Optional info icon click
        iconInfo.setOnClickListener {
            presenter.onInfoClicked()
        }
    }

    override fun showAppName(name: String) {
        textAppName.text = name
    }

    override fun showMainTitle(title: String) {
        textMainTitle.text = title
    }

    override fun showWelcomeText(text: String) {
        textWelcome.text = text
    }

    override fun showDescription(description: String) {
        textDescription.text = description
    }

    override fun navigateToNextScreen() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showInfoDialog() {
        // You can later implement an AlertDialog or Toast here
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
