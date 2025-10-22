package com.example.safegass.connected

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R
import com.example.safegass.settings.SettingsActivity

class ConnectedDevicesActivity : AppCompatActivity(), ConnectedDevicesContract.View {

    private lateinit var recyclerDevices: RecyclerView
    private lateinit var adapter: ConnectedDevicesAdapter
    private lateinit var presenter: ConnectedDevicesPresenter
    private lateinit var inputSerial: EditText
    private lateinit var inputLocation: EditText
    private lateinit var btnAddDevice: Button
    private lateinit var btnCancelAdd: Button
    private lateinit var btnBack: ImageView  // 🔙 top arrow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connected_devices)

        // 🔹 Initialize views
        recyclerDevices = findViewById(R.id.recyclerDevices)
        inputSerial = findViewById(R.id.inputSerial)
        inputLocation = findViewById(R.id.inputLocation)
        btnAddDevice = findViewById(R.id.btnAddDevice)
        btnCancelAdd = findViewById(R.id.btnCancelAdd)
        btnBack = findViewById(R.id.btnMenu) // arrow icon from layout

        // 🔹 Setup RecyclerView
        adapter = ConnectedDevicesAdapter(mutableListOf())
        recyclerDevices.layoutManager = LinearLayoutManager(this)
        recyclerDevices.adapter = adapter

        // 🔹 Initialize Presenter
        presenter = ConnectedDevicesPresenter(this, this)

        // 🔹 Load devices from Firebase
        presenter.loadDevices()

        // 🔹 Add device
        btnAddDevice.setOnClickListener {
            val serial = inputSerial.text.toString().trim()
            val location = inputLocation.text.toString().trim()
            presenter.addDevice(serial, location)
        }

        // 🔹 Cancel input fields
        btnCancelAdd.setOnClickListener {
            inputSerial.text.clear()
            inputLocation.text.clear()
        }

        // 🔹 Back button (arrow icon)
        btnBack.setOnClickListener {
            goBackToSettings()
        }

        // ✅ Handle system back gesture (modern method)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackToSettings()
            }
        })
    }

    // ✅ Function for clean navigation back to SettingsActivity
    private fun goBackToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 🔹 Show device list
    override fun showDevices(devices: List<Device>) {
        adapter.updateDevices(devices)
    }

    // 🔹 Toast messages
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 🔹 Clear input fields after saving
    override fun clearInputFields() {
        inputSerial.text.clear()
        inputLocation.text.clear()
    }
}
