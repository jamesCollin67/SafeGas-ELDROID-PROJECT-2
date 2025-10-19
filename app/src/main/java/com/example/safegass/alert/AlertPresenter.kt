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
                    val alert = child.getValue(Alert::class.java)
                    if (alert != null) {
                        alerts.add(alert)
                    }
                }
                // Optional: sort by time if time format allows (here we keep insertion order)
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
