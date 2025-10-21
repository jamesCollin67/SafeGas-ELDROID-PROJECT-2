package com.example.safegass.history

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryModel {

    private val database = FirebaseDatabase.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val ref = database.getReference("users/$uid/alerts")

    fun fetchHistory(callback: (List<HistoryRecord>) -> Unit, onError: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseDatabase.getInstance().reference

        // Fetch location once first
        db.child("users").child(userId).child("dashboard").child("location")
            .get()
            .addOnSuccessListener { locationSnap ->
                val locationName = locationSnap.getValue(String::class.java) ?: "Unknown"

                // Listen for real-time updates to alerts
                db.child("users").child(userId).child("alerts")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val records = mutableListOf<HistoryRecord>()
                            for (child in snapshot.children) {
                                val status = child.child("status").getValue(String::class.java) ?: ""
                                val ppm = child.child("ppm").getValue(Int::class.java) ?: 0
                                val timestamp = child.child("timestamp").getValue(String::class.java) ?: ""

                                records.add(
                                    HistoryRecord(
                                        status = status,
                                        ppm = ppm,
                                        timestamp = timestamp,
                                        location = locationName
                                    )
                                )
                            }

                            // Sort latest first
                            callback(records.sortedByDescending { it.timestamp })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onError(error.message)
                        }
                    })
            }
            .addOnFailureListener {
                onError("Failed to get location: ${it.message}")
            }
    }
}
