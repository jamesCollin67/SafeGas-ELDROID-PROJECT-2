package com.example.safegass.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.dashboard.DashboardActivity

class ViewPageActivity : AppCompatActivity(), ViewPageContract.View {

    private lateinit var presenter: ViewPageContract.Presenter

    // UI elements
    private lateinit var tvStatusValue: TextView
    private lateinit var tvStatusRight: TextView
    private lateinit var tvBatteryValue: TextView
    private lateinit var tvBatteryRight: TextView
    private lateinit var tvUptimeValue: TextView
    private lateinit var tvUptimeRight: TextView
    private lateinit var tvCalibrationValue: TextView
    private lateinit var tvCalibrationRight: TextView
    private lateinit var tvWifiValue: TextView
    private lateinit var tvWifiRight: TextView
    private lateinit var tvDeviceIdValue: TextView
    private lateinit var tvDeviceIdRight: TextView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)

        presenter = ViewPagePresenter(this, ViewPageRepository())

        // Initialize UI elements
        btnBack = findViewById(R.id.btn_back)
        tvStatusValue = findViewById(R.id.tv_status_value)
        tvStatusRight = findViewById(R.id.tv_status_right)
        tvBatteryValue = findViewById(R.id.tv_battery_value)
        tvBatteryRight = findViewById(R.id.tv_battery_right)
        tvUptimeValue = findViewById(R.id.tv_uptime_value)
        tvUptimeRight = findViewById(R.id.tv_uptime_right)
        tvCalibrationValue = findViewById(R.id.tv_calibration_value)
        tvCalibrationRight = findViewById(R.id.tv_calibration_right)
        tvWifiValue = findViewById(R.id.tv_wifi_value)
        tvWifiRight = findViewById(R.id.tv_wifi_right)
        tvDeviceIdValue = findViewById(R.id.tv_deviceid_value)
        tvDeviceIdRight = findViewById(R.id.tv_deviceid_right)

        btnBack.setOnClickListener {
            presenter.onBackButtonClicked()
        }

        // Load data in real time
        presenter.loadRealtimeData()
    }

    override fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showSensorData(data: SensorData) {
        tvStatusValue.text = data.status
        tvStatusRight.text = if (data.online) "Online" else "Offline"

        tvBatteryValue.text = "${data.batteryLevel}%"
        tvBatteryRight.text = if (data.batteryLevel > 30) "Good" else "Low"

        tvUptimeValue.text = "${data.uptimeDays} days"
        tvUptimeRight.text = "Stable"

        tvCalibrationValue.text = data.lastCalibration
        tvCalibrationRight.text = "Calibrated"

        tvWifiValue.text = "${data.wifiSignal} dBm"
        tvWifiRight.text = if (data.wifiSignal > -70) "Good" else "Weak"

        tvDeviceIdValue.text = data.deviceId
        tvDeviceIdRight.text = "Active"
    }

    override fun showError(message: String) {
        tvStatusValue.text = "Error"
        tvStatusRight.text = message
    }
}
