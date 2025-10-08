package com.example.safegass.connected

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R
import com.example.safegass.settings.SettingsActivity

class ConnectedDevicesActivity : Activity(), ConnectedDevicesContract.View {

    private lateinit var presenter: ConnectedDevicesPresenter
    private lateinit var recyclerDevices: RecyclerView
    private lateinit var btnScanQR: Button
    private lateinit var btnManualEntry: Button
    private lateinit var btnAddDevice: Button
    private lateinit var btnCancelAdd: Button
    private lateinit var inputSerial: EditText
    private lateinit var inputLocation: EditText
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connected_devices)

        presenter = ConnectedDevicesPresenter(this, this)

        recyclerDevices = findViewById(R.id.recyclerDevices)
        btnScanQR = findViewById(R.id.btnScanQR)
        btnManualEntry = findViewById(R.id.btnManualEntry)
        btnAddDevice = findViewById(R.id.btnAddDevice)
        btnCancelAdd = findViewById(R.id.btnCancelAdd)
        inputSerial = findViewById(R.id.inputSerial)
        inputLocation = findViewById(R.id.inputLocation)
        btnBack = findViewById(R.id.btnMenu)

        recyclerDevices.layoutManager = LinearLayoutManager(this)
        recyclerDevices.adapter = ConnectedDevicesAdapter(mutableListOf())

        presenter.loadDevices()

        btnAddDevice.setOnClickListener {
            val serial = inputSerial.text.toString()
            val location = inputLocation.text.toString()
            presenter.addDevice(serial, location)
        }

        btnCancelAdd.setOnClickListener {
            inputSerial.text.clear()
            inputLocation.text.clear()
            Toast.makeText(this, "Add device canceled", Toast.LENGTH_SHORT).show()
        }

        btnScanQR.setOnClickListener {
            presenter.scanQRCode()
        }

        btnManualEntry.setOnClickListener {
            Toast.makeText(this, "Enter serial number manually", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    override fun showDevices(devices: List<Device>) {
        (recyclerDevices.adapter as ConnectedDevicesAdapter).updateDevices(devices)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
