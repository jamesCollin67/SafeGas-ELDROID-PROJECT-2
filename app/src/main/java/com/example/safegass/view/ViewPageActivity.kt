package com.example.safegass.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R
import com.example.safegass.dashboard.DashboardActivity

class ViewPageActivity : AppCompatActivity(), ViewPageContract.View {

    private lateinit var presenter: ViewPagePresenter
    private lateinit var adapter: LeakHistoryAdapter

    private lateinit var tvStatus: TextView
    private lateinit var tvOnline: TextView
    private lateinit var tvBattery: TextView
    private lateinit var tvUptime: TextView
    private lateinit var tvCalibration: TextView
    private lateinit var tvWifi: TextView
    private lateinit var tvDeviceId: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var btnBack: ImageButton  // ✅ add reference for your back button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)

        presenter = ViewPagePresenter(this)
        adapter = LeakHistoryAdapter(emptyList())

        tvStatus = findViewById(R.id.tv_status_value)
        tvOnline = findViewById(R.id.tv_status_right)
        tvBattery = findViewById(R.id.tv_battery_value)
        tvUptime = findViewById(R.id.tv_uptime_value)
        tvCalibration = findViewById(R.id.tv_calibration_value)
        tvWifi = findViewById(R.id.tv_wifi_value)
        tvDeviceId = findViewById(R.id.tv_deviceid_value)
        recycler = findViewById(R.id.recycler_history)
        btnBack = findViewById(R.id.btn_back)  // ✅ make sure this matches your XML id

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // ✅ navigate back to dashboard when back button is clicked
        btnBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        // Load device details and leak history
        presenter.loadDeviceDetails()
        presenter.loadLeakHistory()
    }

    override fun showDeviceDetails(details: DeviceDetails) {
        tvStatus.text = details.status
        tvOnline.text = details.online
        tvBattery.text = details.batteryLevel
        tvUptime.text = details.upTimeDays
        tvCalibration.text = details.lastCalibration
        tvWifi.text = details.wifiSignal
        tvDeviceId.text = details.deviceId
    }

    override fun showLeakHistory(historyList: List<LeakHistoryRecord>) {
        adapter.updateData(historyList)
    }

    override fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }
}
