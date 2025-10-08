package com.example.safegass.alert

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R

class AlertActivity : AppCompatActivity(), AlertContract.View {

    private lateinit var presenter: AlertContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlertAdapter

    // Buttons from Emergency Alert Card
    private lateinit var btnMuteAlarm: Button
    private lateinit var btnViewDetails: Button
    private lateinit var btnCallEmergency: Button

    // Tabs
    private lateinit var tabAll: TextView
    private lateinit var tabDanger: TextView
    private lateinit var tabWarning: TextView
    private lateinit var tabInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_gas)

        // Initialize buttons
        btnMuteAlarm = findViewById(R.id.btnMuteAlarm)
        btnViewDetails = findViewById(R.id.btnViewDetails)
        btnCallEmergency = findViewById(R.id.btnCallEmergency)

        // Initialize tabs
        tabAll = findViewById(R.id.tabAll)
        tabDanger = findViewById(R.id.tabDanger)
        tabWarning = findViewById(R.id.tabWarning)
        tabInfo = findViewById(R.id.tabInfo)

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerAlerts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AlertAdapter(emptyList(), object : AlertAdapter.AlertActionListener {
            override fun onMuteClicked(alertId: Int) {
                presenter.muteAlarm(alertId)
            }

            override fun onCallEmergencyClicked(alertId: Int) {
                presenter.callEmergency(alertId)
            }
        })
        recyclerView.adapter = adapter

        // Presenter setup
        presenter = AlertPresenter(AlertModel())
        presenter.attachView(this)
        presenter.loadAlerts()

        // Emergency card buttons
        btnMuteAlarm.setOnClickListener { presenter.muteAlarm(1) } // Example
        btnCallEmergency.setOnClickListener { presenter.callEmergency(1) } // Example
    }

    override fun showAlerts(alerts: List<Alert>) {
        adapter.updateAlerts(alerts)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() { /* TODO: show progress */ }

    override fun hideLoading() { /* TODO: hide progress */ }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
