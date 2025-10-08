package com.example.safegass.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.settings.SettingsActivity

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    private lateinit var textPPM: TextView
    private lateinit var textStatus: TextView
    private lateinit var textLocation: TextView
    private lateinit var textLastUpdated: TextView

    private lateinit var textActiveAlerts: TextView
    private lateinit var textOnlineDevices: TextView
    private lateinit var textAveragePPM: TextView
    private lateinit var textPeakPPM: TextView

    private lateinit var buttonViewDetails: Button
    private lateinit var buttonCallEmergency: Button
    private lateinit var buttonMuteAlarm: Button
    private lateinit var menuIcon: ImageView

    // Bottom nav buttons
    private lateinit var navDashboard: LinearLayout
    private lateinit var navAlerts: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this)

        // Initialize views
        textPPM = findViewById(R.id.textPPM)
        textStatus = findViewById(R.id.textStatus)
        textLocation = findViewById(R.id.textLocation)
        textLastUpdated = findViewById(R.id.textLastUpdated)

        textActiveAlerts = findViewById(R.id.textActiveAlerts)
        textOnlineDevices = findViewById(R.id.textOnlineDevices)
        textAveragePPM = findViewById(R.id.textAveragePPM)
        textPeakPPM = findViewById(R.id.textPeakPPM)

        buttonViewDetails = findViewById(R.id.buttonViewDetails)
        buttonCallEmergency = findViewById(R.id.buttonCallEmergency)
        buttonMuteAlarm = findViewById(R.id.buttonMuteAlarm)
        menuIcon = findViewById(R.id.menuIcon)

        // Bottom nav initialization
        navDashboard = findViewById(R.id.navDashboard)
        navAlerts = findViewById(R.id.navAlerts)
        navHistory = findViewById(R.id.navHistory)
        navSettings = findViewById(R.id.navSettings)

        // === Navigation handling ===
        navDashboard.setOnClickListener {
            // Already on dashboard, maybe do nothing or refresh
        }

        navAlerts.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
            overridePendingTransition(0, 0)
        }

        navHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            overridePendingTransition(0, 0)
        }

        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // Load dashboard data
        presenter.loadDashboardData()
    }

    // ==== Contract View Methods ====
    override fun showPPM(ppm: String, status: String, location: String) {
        textPPM.text = ppm
        textStatus.text = status
        textLocation.text = location
    }

    override fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPpm: String, peakPpm: String) {
        textActiveAlerts.text = activeAlerts.toString()
        textOnlineDevices.text = onlineDevices.toString()
        textAveragePPM.text = avgPpm
        textPeakPPM.text = peakPpm
    }

    override fun showLastUpdated(time: String) {
        textLastUpdated.text = time
    }
}
