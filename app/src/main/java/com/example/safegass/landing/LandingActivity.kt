package com.example.safegass.landing

import android.app.Activity
import android.app.AlertDialog
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

        // Initialize MVP components
        presenter = LandingPresenter(LandingModel())
        presenter.attachView(this)

        // Load landing page content
        presenter.loadLandingContent()

        // Set button listeners
        buttonGetStarted.setOnClickListener {
            presenter.onGetStartedClicked()
        }

        iconInfo.setOnClickListener {
            presenter.onInfoClicked()
        }
    }

    // View implementations
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
        finish() // optional: closes the landing screen
    }

    override fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("About SafeGass")
            .setMessage("SafeGass helps detect and monitor gas leaks to ensure your family's safety.")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}
