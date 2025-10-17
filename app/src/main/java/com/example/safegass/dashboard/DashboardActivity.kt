package com.example.safegass.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.settings.SettingsActivity
import com.example.safegass.view.ViewPageActivity
import androidx.core.graphics.toColorInt

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardContract.Presenter

    // Views from layout
    private lateinit var menuIcon: ImageView
    private lateinit var textAppName: TextView
    private lateinit var imageHouseGas: ImageView

    private lateinit var textPPM: TextView
    private lateinit var textStatus: TextView
    private lateinit var textLocation: TextView
    private lateinit var buttonViewDetails: Button

    private lateinit var buttonCallEmergency: Button
    private lateinit var buttonMuteAlarm: Button

    private lateinit var textLastUpdated: TextView

    private lateinit var textActiveAlerts: TextView
    private lateinit var textOnlineDevices: TextView
    private lateinit var textAveragePPM: TextView
    private lateinit var textPeakPPM: TextView

    // bottom nav
    private lateinit var navDashboard: LinearLayout
    private lateinit var navAlerts: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Bind all views
        menuIcon = findViewById(R.id.menuIcon)
        textAppName = findViewById(R.id.textAppName)
        imageHouseGas = findViewById(R.id.imageHouseGas)

        textPPM = findViewById(R.id.textPPM)
        textStatus = findViewById(R.id.textStatus)
        textLocation = findViewById(R.id.textLocation)
        buttonViewDetails = findViewById(R.id.buttonViewDetails)

        buttonCallEmergency = findViewById(R.id.buttonCallEmergency)
        buttonMuteAlarm = findViewById(R.id.buttonMuteAlarm)

        textLastUpdated = findViewById(R.id.textLastUpdated)

        textActiveAlerts = findViewById(R.id.textActiveAlerts)
        textOnlineDevices = findViewById(R.id.textOnlineDevices)
        textAveragePPM = findViewById(R.id.textAveragePPM)
        textPeakPPM = findViewById(R.id.textPeakPPM)

        navDashboard = findViewById(R.id.navDashboard)
        navAlerts = findViewById(R.id.navAlerts)
        navHistory = findViewById(R.id.navHistory)
        navSettings = findViewById(R.id.navSettings)

        // MVP setup
        presenter = DashboardPresenter(this, DashboardRepository())

        // Buttons
        buttonViewDetails.setOnClickListener {
            startActivity(Intent(this, ViewPageActivity::class.java))
        }
        buttonCallEmergency.setOnClickListener {
            Toast.makeText(this, "Calling emergency services...", Toast.LENGTH_SHORT).show()
        }
        buttonMuteAlarm.setOnClickListener {
            Toast.makeText(this, "Alarm muted temporarily.", Toast.LENGTH_SHORT).show()
        }

        // Bottom navigation
        navAlerts.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
        navHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    // --- View interface ---
    override fun showLoading(isLoading: Boolean) {
        textLastUpdated.text = if (isLoading) "Loading..." else textLastUpdated.text
    }

    override fun showPPM(ppm: String) {
        textPPM.text = ppm
    }

    override fun showStatus(status: String) {
        textStatus.text = status
        when (status.lowercase()) {
            "safe" -> textStatus.setTextColor("#2E7D32".toColorInt())
            "warning" -> textStatus.setTextColor("#FBC02D".toColorInt())
            "danger" -> textStatus.setTextColor("#D32F2F".toColorInt())
            else -> textStatus.setTextColor("#000000".toColorInt())
        }
    }

    override fun showLocation(location: String) {
        textLocation.text = location
    }

    override fun showLastUpdated(time: String) {
        textLastUpdated.text = if (time.isBlank()) "" else "Last updated $time"
    }

    override fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPpm: String, peakPpm: String) {
        textActiveAlerts.text = activeAlerts.toString()
        textOnlineDevices.text = onlineDevices.toString()
        textAveragePPM.text = avgPpm
        textPeakPPM.text = peakPpm
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
