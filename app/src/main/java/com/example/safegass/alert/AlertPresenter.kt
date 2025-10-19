package com.example.safegass.alert

import com.google.firebase.database.*
import java.util.*

class AlertPresenter(private val view: AlertContract.View) : AlertContract.Presenter {

    private val database = FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private val alertsRef: DatabaseReference = database.getReference("alerts")
    private var listener: ValueEventListener? = null

    override fun loadAlerts() {
        // remove previous listener if present
        listener?.let { alertsRef.removeEventListener(it) }

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alerts = mutableListOf<Alert>()
                for (child in snapshot.children) {
                    // ✅ Read each field safely, with correct types
                    val type = child.child("type").getValue(String::class.java) ?: ""
                    val title = child.child("title").getValue(String::class.java) ?: ""
                    val description = child.child("description").getValue(String::class.java) ?: ""
                    val source = child.child("source").getValue(String::class.java) ?: ""

                    // ✅ Try to get timestamp as Long or String
                    val timeValue = child.child("time").value
                    val time = when (timeValue) {
                        is Long -> timeValue
                        is Double -> timeValue.toLong()
                        is String -> timeValue.toLongOrNull() ?: 0L
                        else -> 0L
                    }

                    alerts.add(Alert(type, title, description, source, time))
                }

                view.showAlerts(alerts)
            }

            override fun onCancelled(error: DatabaseError) {
                view.showError("Failed to load alerts: ${error.message}")
            }
        }

        alertsRef.addValueEventListener(listener as ValueEventListener)
    }

    override fun removeListeners() {
        listener?.let { alertsRef.removeEventListener(it) }
        listener = null
    }
}
