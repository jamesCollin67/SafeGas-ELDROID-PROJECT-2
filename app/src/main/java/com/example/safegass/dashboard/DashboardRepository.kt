package com.example.safegass.dashboard

import com.google.firebase.database.*

class DashboardRepository {

    private val databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("dashboard")

    private var listener: ValueEventListener? = null

    fun loadDashboardData(callback: (DashboardData?) -> Unit, onError: (String) -> Unit) {
        listener = databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue(DashboardData::class.java)
                    callback(data)
                } else {
                    onError("No dashboard data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }

    fun saveLocation(location: String, imageUrl: String?, onComplete: () -> Unit, onError: (String) -> Unit) {
        val updateMap = mapOf(
            "location" to location,
            "imageUrl" to (imageUrl ?: "")
        )
        databaseRef.updateChildren(updateMap)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { onError(it.message ?: "Failed to save location") }
    }

    fun removeListeners() {
        listener?.let { databaseRef.removeEventListener(it) }
    }
}
