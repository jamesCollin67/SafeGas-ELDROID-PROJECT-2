package com.example.safegass.dashboard

import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class DashboardRepository {

    private val database = FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private val dashboardRef = database.getReference("dashboard")
    private val storage = FirebaseStorage.getInstance().reference.child("dashboard_images")

    fun fetchDashboardData(callback: (DashboardData?) -> Unit) {
        dashboardRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(DashboardData::class.java)
                callback(data)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun uploadImage(imageUri: Uri, onComplete: (String?) -> Unit) {
        val fileRef = storage.child("${System.currentTimeMillis()}.jpg")
        val uploadTask = fileRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }
        }.addOnFailureListener {
            onComplete(null)
        }
    }

    fun saveLocation(location: String, imageUrl: String?) {
        val dataMap = mapOf(
            "location" to location,
            "imageUrl" to imageUrl
        )
        dashboardRef.updateChildren(dataMap)
    }
}
