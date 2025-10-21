package com.example.safegass.history

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryModel {

    private val database = FirebaseDatabase.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val ref = database.getReference("users/$uid/alerts")

    fun fetchHistory(callback: (List<HistoryRecord>) -> Unit, onError: (String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val records = mutableListOf<HistoryRecord>()
                for (child in snapshot.children) {
                    val status = child.child("status").getValue(String::class.java) ?: ""
                    val ppm = child.child("ppm").getValue(Int::class.java) ?: 0
                    val timestamp = child.child("timestamp").getValue(String::class.java) ?: ""
                    val location = child.child("location").getValue(String::class.java) ?: ""
                    records.add(HistoryRecord(status, ppm, timestamp, location))
                }
                callback(records)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}
