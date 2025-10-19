package com.example.safegass.view

import com.google.firebase.database.*

class ViewPageRepository : ViewPageContract.Repository {

    private val databaseRef = FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
        .getReference("deviceDetails")

    override fun listenToSensorData(callback: (SensorData?) -> Unit, onError: (String) -> Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(SensorData::class.java)
                callback(data)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}
