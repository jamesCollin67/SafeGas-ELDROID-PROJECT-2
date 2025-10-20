package com.example.safegass.alert

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AlertPresenter(private val view: AlertContract.View) : AlertContract.Presenter {

    private val database =
        FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private var listener: ValueEventListener? = null
    private var dashboardListener: ValueEventListener? = null

    override fun loadAlerts() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val alertsRef = database.getReference("users/$uid/alerts")
        val dashboardRef = database.getReference("users/$uid/dashboard")

        // ✅ Listen for alerts uploaded by Arduino
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alerts = mutableListOf<Alert>()

                for (child in snapshot.children) {
                    val type = child.child("status").getValue(String::class.java) ?: "Unknown"
                    val ppm = child.child("ppm").getValue(Int::class.java) ?: 0
                    val timeString = child.child("timestamp").getValue(String::class.java) ?: ""
                    val time = System.currentTimeMillis() / 1000 // fallback if parsing fails

                    val title = "Gas Status: $type"
                    val description = "Detected gas concentration: $ppm ppm"
                    val source = "Device 1"

                    alerts.add(Alert(type, title, description, source, time, ppm))
                }

                alerts.sortByDescending { it.time } // newest first
                view.showAlerts(alerts)
            }

            override fun onCancelled(error: DatabaseError) {
                view.showError("Failed to load alerts: ${error.message}")
            }
        }
        alertsRef.addValueEventListener(listener as ValueEventListener)

        // ✅ Dashboard listener for top card
        dashboardListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.child("location").getValue(String::class.java) ?: "Unknown"
                val ppm = snapshot.child("ppm").getValue(Int::class.java) ?: 0
                val status = when {
                    ppm < 100 -> "Safe"
                    ppm in 100..300 -> "Warning"
                    else -> "Danger"
                }

                val alert = Alert(
                    type = status,
                    title = "Current Status",
                    description = "Gas reading at $location: $ppm ppm",
                    source = location,
                    time = System.currentTimeMillis() / 1000,
                    ppm = ppm
                )

                view.showDashboardStatus(alert)
            }

            override fun onCancelled(error: DatabaseError) {
                view.showError("Dashboard data failed: ${error.message}")
            }
        }
        dashboardRef.addValueEventListener(dashboardListener as ValueEventListener)
    }

    override fun removeListeners() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val alertsRef = database.getReference("users/$uid/alerts")
        val dashboardRef = database.getReference("users/$uid/dashboard")

        listener?.let { alertsRef.removeEventListener(it) }
        dashboardListener?.let { dashboardRef.removeEventListener(it) }
    }
}
