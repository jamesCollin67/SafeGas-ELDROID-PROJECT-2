package com.example.safegass.history

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.dashboard.DashboardActivity
import com.example.safegass.settings.SettingsActivity

class HistoryActivity : AppCompatActivity(), HistoryContract.View {

    private lateinit var presenter: HistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        presenter = HistoryPresenter(this)

        // --- Navigation bar references ---
        val btnDashboard = findViewById<LinearLayout>(R.id.navDashboard)
        val btnAlert = findViewById<LinearLayout>(R.id.navAlerts)
        val btnSettings = findViewById<LinearLayout>(R.id.navSettings)
        val btnHistory = findViewById<LinearLayout>(R.id.navHistory) // current page

        // --- Set click listeners ---
        btnDashboard.setOnClickListener { presenter.onDashboardClicked() }
        btnAlert.setOnClickListener { presenter.onAlertClicked() }
        btnSettings.setOnClickListener { presenter.onSettingsClicked() }
        btnHistory.setOnClickListener { /* Stay on current page */ }
    }

    // --- Navigation implementations ---
    override fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    override fun navigateToAlert() {
        startActivity(Intent(this, AlertActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    override fun navigateToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }
}
