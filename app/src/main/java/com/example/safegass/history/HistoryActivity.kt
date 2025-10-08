package com.example.safegass.history

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.alert.AlertContract
import com.example.safegass.dashboard.DashboardContract
import com.example.safegass.settings.SettingsContract

class HistoryActivity : AppCompatActivity(), HistoryContract.View {

    private lateinit var presenter: HistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        presenter = HistoryPresenter(this)

        val btnDashboard = findViewById<LinearLayout>(R.id.navDashboard)
        val btnAlert = findViewById<LinearLayout>(R.id.navAlerts)
        val btnSettings = findViewById<LinearLayout>(R.id.navSettings)

        btnDashboard.setOnClickListener { presenter.onDashboardClicked() }
        btnAlert.setOnClickListener { presenter.onAlertClicked() }
        btnSettings.setOnClickListener { presenter.onSettingsClicked() }
    }

    override fun navigateToDashboard() {
        startActivity(Intent(this, DashboardContract::class.java))
        overridePendingTransition(0, 0)
    }

    override fun navigateToAlert() {
        startActivity(Intent(this, AlertContract::class.java))
        overridePendingTransition(0, 0)
    }

    override fun navigateToSettings() {
        startActivity(Intent(this, SettingsContract::class.java))
        overridePendingTransition(0, 0)
    }
}
