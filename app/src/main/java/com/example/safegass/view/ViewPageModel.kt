package com.example.safegass.view

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewPageModel {

    private val db = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun fetchDeviceDetails(callback: (DeviceDetails?) -> Unit, onError: (String) -> Unit) {
        if (userId == null) return onError("User not logged in")

        db.child("deviceDetails")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        onError("Device details unavailable (path empty)")
                        return
                    }

                    val status = snapshot.child("status").value?.toString() ?: ""
                    val online = snapshot.child("online").value?.toString() ?: ""
                    val batteryLevel = snapshot.child("batteryLevel").value?.toString() ?: ""
                    val upTimeDays = snapshot.child("upTimeDays").value?.toString() ?: ""
                    val lastCalibration = snapshot.child("lastCalibration").value?.toString() ?: ""
                    val wifiSignal = snapshot.child("wifiSignal").value?.toString() ?: ""
                    val deviceId = snapshot.child("deviceId").value?.toString() ?: ""

                    val details = DeviceDetails(
                        status = status,
                        online = online,
                        batteryLevel = batteryLevel,
                        upTimeDays = upTimeDays,
                        lastCalibration = lastCalibration,
                        wifiSignal = wifiSignal,
                        deviceId = deviceId
                    )

                    callback(details)
                }


                override fun onCancelled(error: DatabaseError) {
                    onError("Firebase error: ${error.message}")
                }
            })
    }




    fun fetchLeakHistory(callback: (List<LeakHistoryRecord>) -> Unit, onError: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseDatabase.getInstance().reference

        // Get leak history (alerts equivalent)
        db.child("users").child(userId).child("alerts")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Get location from dashboard node
                    db.child("users").child(userId).child("dashboard").child("location")
                        .get()
                        .addOnSuccessListener { locationSnap ->
                            val locationName = locationSnap.getValue(String::class.java) ?: "Unknown"
                            val records = mutableListOf<LeakHistoryRecord>()

                            for (child in snapshot.children) {
                                val status = child.child("status").getValue(String::class.java) ?: ""
                                val ppm = child.child("ppm").getValue(Int::class.java) ?: 0
                                val timestamp = child.child("timestamp").getValue(String::class.java) ?: ""

                                records.add(LeakHistoryRecord(ppm, timestamp, status, locationName))
                            }

                            callback(records.reversed()) // optional: reverse to show latest first
                        }
                        .addOnFailureListener {
                            onError("Failed to get location: ${it.message}")
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }


}
