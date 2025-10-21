package com.example.safegass.connected

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R

class ConnectedDevicesActivity : AppCompatActivity(), ConnectedDevicesContract.View {

    private lateinit var presenter: ConnectedDevicesPresenter
    private lateinit var adapter: ConnectedDevicesAdapter

    private lateinit var inputSerial: EditText
    private lateinit var inputLocation: EditText
    private lateinit var recyclerDevices: RecyclerView
    private lateinit var btnAddDevice: Button
    private lateinit var btnCancelAdd: Button
    private lateinit var btnScanQR: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connected_devices)

        // 🔹 Initialize Views
        inputSerial = findViewById(R.id.inputSerial)
        inputLocation = findViewById(R.id.inputLocation)
        recyclerDevices = findViewById(R.id.recyclerDevices)
        btnAddDevice = findViewById(R.id.btnAddDevice)
        btnCancelAdd = findViewById(R.id.btnCancelAdd)
        btnScanQR = findViewById(R.id.btnScanQR)

        // 🔹 Set up RecyclerView
        adapter = ConnectedDevicesAdapter(mutableListOf())
        recyclerDevices.layoutManager = LinearLayoutManager(this)
        recyclerDevices.adapter = adapter

        // 🔹 Presenter
        presenter = ConnectedDevicesPresenter(this, this)
        presenter.loadDevices()

        // 🔹 Add Device
        btnAddDevice.setOnClickListener {
            val serial = inputSerial.text.toString()
            val location = inputLocation.text.toString()
            presenter.addDevice(serial, location)
        }

        // 🔹 Cancel Button
        btnCancelAdd.setOnClickListener {
            clearInputFields()
        }

        // 🔹 Scan QR Button
        btnScanQR.setOnClickListener {
            presenter.scanQRCode()
        }
    }

    // 🔹 Displays list of devices
    override fun showDevices(devices: List<Device>) {
        adapter.updateDevices(devices)
    }

    // 🔹 Shows messages via Toast
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 🔹 Clears input fields after adding or canceling
    override fun clearInputFields() {
        inputSerial.text.clear()
        inputLocation.text.clear()
    }
}
