package com.example.safegass.connected

import com.google.firebase.database.*
import android.content.Context

class ConnectedDevicesPresenter(
    private val view: ConnectedDevicesContract.View,
    context: Context
) : ConnectedDevicesContract.Presenter {

    private val dbRef = FirebaseDatabase.getInstance().getReference("devices")

    override fun loadDevices() {
        // Fetch connected devices from Firebase
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val devices = mutableListOf<Device>()
                for (child in snapshot.children) {
                    val serial = child.child("serial").getValue(String::class.java) ?: ""
                    val location = child.child("location").getValue(String::class.java) ?: ""
                    if (serial.isNotEmpty() && location.isNotEmpty()) {
                        devices.add(Device(serial, location))
                    }
                }

                if (devices.isNotEmpty()) {
                    view.showDevices(devices)
                } else {
                    view.showMessage("No connected devices found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                view.showMessage("Failed to load devices: ${error.message}")
            }
        })
    }

    override fun addDevice(serial: String, location: String) {
        if (serial.isBlank() || location.isBlank()) {
            view.showMessage("Please enter both serial and location.")
            return
        }

        val newDevice = Device(serial, location)
        val newRef = dbRef.push()
        newRef.setValue(newDevice)
            .addOnSuccessListener {
                view.showMessage("Device added successfully.")
                view.clearInputFields() // ✅ clear fields after saving
            }
            .addOnFailureListener {
                view.showMessage("Failed to add device: ${it.message}")
            }
    }


    override fun scanQRCode() {
        view.showMessage("Scanning QR Code... (feature coming soon)")
    }
}
