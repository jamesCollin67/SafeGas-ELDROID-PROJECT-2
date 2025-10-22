package com.example.safegass.connected

import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseAuth

class ConnectedDevicesPresenter(
    private val view: ConnectedDevicesContract.View,
    private val context: android.content.Context
) : ConnectedDevicesContract.Presenter {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().reference

    private val devicesList = mutableListOf<Device>()

    override fun loadDevices() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            view.showMessage("❌ User not logged in.")
            return
        }

        val userDevicesRef = dbRef.child("users").child(uid).child("devices")

        // ✅ Listen for realtime device updates
        userDevicesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val device = snapshot.getValue(Device::class.java)
                if (device != null) {
                    devicesList.add(device)
                    view.showDevices(devicesList)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updated = snapshot.getValue(Device::class.java) ?: return
                val index = devicesList.indexOfFirst { it.serial == updated.serial }
                if (index != -1) {
                    devicesList[index] = updated
                    view.showDevices(devicesList)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removed = snapshot.getValue(Device::class.java) ?: return
                val index = devicesList.indexOfFirst { it.serial == removed.serial }
                if (index != -1) {
                    devicesList.removeAt(index)
                    view.showDevices(devicesList)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                view.showMessage("❌ Firebase error: ${error.message}")
            }
        })
    }

    override fun addDevice(serial: String, location: String) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            view.showMessage("❌ User not logged in.")
            return
        }

        val device = Device(serial, location, "Online")
        dbRef.child("users").child(uid).child("devices").child(serial)
            .setValue(device)
            .addOnSuccessListener {
                view.showMessage("✅ Device added successfully.")
                view.clearInputFields()
            }
            .addOnFailureListener {
                view.showMessage("❌ Failed: ${it.message}")
            }
    }

    override fun scanQRCode() {
        view.showMessage("QR Code scanning feature coming soon.")
    }
}
