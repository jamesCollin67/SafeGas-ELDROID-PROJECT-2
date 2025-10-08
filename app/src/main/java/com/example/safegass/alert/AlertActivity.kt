package com.example.safegass.alert

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_gas)

        // --- RecyclerView setup ---
        recyclerAlerts = findViewById(R.id.recyclerAlerts)
        adapter = AlertAdapter(emptyList())
        recyclerAlerts.layoutManager = LinearLayoutManager(this)
        recyclerAlerts.adapter = adapter

        // --- Presenter setup ---
        presenter = AlertPresenter(this)
        presenter.loadAlerts()

        // --- Navigation bar setup ---
        setupNavigationBar()
    }

    private fun setupNavigationBar() {
        val navDashboard = findViewById<LinearLayout>(R.id.navDashboard)
        val navAlerts = findViewById<LinearLayout>(R.id.navAlerts)
        val navHistory = findViewById<LinearLayout>(R.id.navHistory)
        val navSettings = findViewById<LinearLayout>(R.id.navSettings)

        navDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navAlerts.setOnClickListener {
            // already in AlertActivity, do nothing
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
        adapter.updateAlerts(alerts)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
