package com.example.safegass.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.settings.SettingsActivity
import com.example.safegass.view.ViewPageActivity   // ✅ Correct import

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    // Top/feature buttons
    private lateinit var buttonViewDetails: Button
    private lateinit var buttonCallEmergency: Button
    private lateinit var buttonMuteAlarm: Button
    private lateinit var menuIcon: ImageView
    private lateinit var progressBar: ProgressBar

    // Bottom navigation
    private lateinit var navDashboard: LinearLayout
    private lateinit var navAlerts: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this)
        initViews()
        initListeners()

        presenter.loadDashboardData()
    }

    private fun initViews() {
        // Feature buttons
        buttonViewDetails = findViewById(R.id.buttonViewDetails)
        buttonCallEmergency = findViewById(R.id.buttonCallEmergency)
        buttonMuteAlarm = findViewById(R.id.buttonMuteAlarm)
        menuIcon = findViewById(R.id.menuIcon)

        progressBar = ProgressBar(this).apply {
            isIndeterminate = true
            visibility = ProgressBar.GONE
        }

        // Bottom navigation
        navDashboard = findViewById(R.id.navDashboard)
        navAlerts = findViewById(R.id.navAlerts)
        navHistory = findViewById(R.id.navHistory)
        navSettings = findViewById(R.id.navSettings)
    }

    private fun initListeners() {
        // Bottom nav listeners
        navDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

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

        // Feature buttons
        buttonViewDetails.setOnClickListener { presenter.onViewDetailsClicked() }
        buttonMuteAlarm.setOnClickListener { presenter.onMuteAlarmClicked() }
        buttonCallEmergency.setOnClickListener { presenter.onCallEmergencyClicked() }
    }

    // ==== Contract Methods ====
    override fun showDetailsPage() {
        val intent = Intent(this, ViewPageActivity::class.java)
        startActivity(intent)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) ProgressBar.VISIBLE else ProgressBar.GONE
    }

    override fun showPPM(ppm: String, status: String, location: String) {
        findViewById<TextView>(R.id.textPPM).text = ppm
        findViewById<TextView>(R.id.textStatus).text = status
        findViewById<TextView>(R.id.textLocation).text = location
    }

    override fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPpm: String, peakPpm: String) {
        findViewById<TextView>(R.id.textActiveAlerts).text = activeAlerts.toString()
        findViewById<TextView>(R.id.textOnlineDevices).text = onlineDevices.toString()
        findViewById<TextView>(R.id.textAveragePPM).text = avgPpm
        findViewById<TextView>(R.id.textPeakPPM).text = peakPpm
    }

    override fun showLastUpdated(time: String) {
        findViewById<TextView>(R.id.textLastUpdated).text = time
    }
}
