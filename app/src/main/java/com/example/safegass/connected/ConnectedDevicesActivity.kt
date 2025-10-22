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
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connected_devices)

        recyclerDevices = findViewById(R.id.recyclerDevices)
        inputSerial = findViewById(R.id.inputSerial)
        inputLocation = findViewById(R.id.inputLocation)
        btnAddDevice = findViewById(R.id.btnAddDevice)
        btnCancelAdd = findViewById(R.id.btnCancelAdd)
        btnBack = findViewById(R.id.btnMenu)

        adapter = ConnectedDevicesAdapter(mutableListOf())
        recyclerDevices.layoutManager = LinearLayoutManager(this)
        recyclerDevices.adapter = adapter

        presenter = ConnectedDevicesPresenter(this, this)
        presenter.loadDevices()

        btnAddDevice.setOnClickListener {
            val serial = inputSerial.text.toString().trim()
            val location = inputLocation.text.toString().trim()
            presenter.addDevice(serial, location)
        }

        btnCancelAdd.setOnClickListener { clearInputFields() }

        btnBack.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ConnectedDevicesActivity, SettingsActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    override fun showDevices(devices: List<Device>) {
        adapter.updateDevices(devices)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun clearInputFields() {
        inputSerial.text.clear()
        inputLocation.text.clear()
    }
}
