package com.example.safegass.alert

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R
import com.example.safegass.dashboard.DashboardActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.settings.SettingsActivity

class AlertActivity : AppCompatActivity(), AlertContract.View {

    private lateinit var presenter: AlertContract.Presenter
    private lateinit var recyclerAlerts: RecyclerView
    private lateinit var adapter: AlertAdapter

    // Top card views
    private lateinit var alertTitle: TextView
    private lateinit var alertDescription: TextView
    private lateinit var alertSource: TextView
    private lateinit var alertTime: TextView

    // Tabs
    private lateinit var tabAll: TextView
    private lateinit var tabDanger: TextView
    private lateinit var tabWarning: TextView
    private lateinit var tabInfo: TextView

    // bottom nav (linear layouts in your include)
    private lateinit var navDashboard: LinearLayout
    private lateinit var navAlerts: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    private var allAlerts: List<Alert> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_gas)

        // Top card bind
        alertTitle = findViewById(R.id.alertTitle)
        alertDescription = findViewById(R.id.alertDescription)
        alertSource = findViewById(R.id.alertSource)
        alertTime = findViewById(R.id.alertTime)

        // RecyclerView setup
        recyclerAlerts = findViewById(R.id.recyclerAlerts)
        adapter = AlertAdapter(emptyList())
        recyclerAlerts.layoutManager = LinearLayoutManager(this)
        recyclerAlerts.adapter = adapter

        // Tabs
        tabAll = findViewById(R.id.tabAll)
        tabDanger = findViewById(R.id.tabDanger)
        tabWarning = findViewById(R.id.tabWarning)
        tabInfo = findViewById(R.id.tabSafe)

        // bottom nav (IDs must exist in included layout)
        navDashboard = findViewById(R.id.navDashboard)
        navAlerts = findViewById(R.id.navAlerts)
        navHistory = findViewById(R.id.navHistory)
        navSettings = findViewById(R.id.navSettings)

        setupNavigationBar()

        // Presenter
        presenter = AlertPresenter(this)
        presenter.loadAlerts()

        setupTabClicks()
    }

    private fun setupTabClicks() {
        tabAll.setOnClickListener { adapter.updateAlerts(allAlerts) }
        tabDanger.setOnClickListener { adapter.updateAlerts(allAlerts.filter { it.type.equals("Danger", true) }) }
        tabWarning.setOnClickListener { adapter.updateAlerts(allAlerts.filter { it.type.equals("Warning", true) }) }
        tabInfo.setOnClickListener { adapter.updateAlerts(allAlerts.filter { it.type.equals("Info", true) }) }
    }

    private fun setupNavigationBar() {
        navDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navAlerts.setOnClickListener {
            // already here
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

    override fun showAlerts(alerts: List<Alert>) {
        allAlerts = alerts
        // show latest alert on top card if there are any (use last inserted as latest)
        if (alerts.isNotEmpty()) {
            val latest = alerts.last()
            alertTitle.text = latest.title
            // fallback when description empty
            alertDescription.text = if (latest.description.isNotBlank()) latest.description else latest.type
            alertSource.text = latest.source
            alertTime.text = latest.time
        } else {
            alertTitle.text = "No alerts"
            alertDescription.text = ""
            alertSource.text = ""
            alertTime.text = ""
        }

        adapter.updateAlerts(allAlerts)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // stop listening to Firebase to avoid memory leak
        if (presenter is AlertPresenter) {
            (presenter as AlertPresenter).removeListeners()
        } else {
            presenter.removeListeners()
        }
    }
}
