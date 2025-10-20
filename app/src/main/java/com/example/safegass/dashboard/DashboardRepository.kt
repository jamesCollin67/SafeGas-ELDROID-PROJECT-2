package com.example.safegass.dashboard

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class DashboardRepository : DashboardContract.Repository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun getDashboardData(callback: (DashboardContract.DashboardData?, DatabaseError?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ref = database.getReference("users/$userId/dashboard")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(DashboardContract.DashboardData::class.java)
                callback(data, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error)
            }
        })
    }


    override fun saveLocation(location: String, callback: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ref = database.getReference("users/$userId/dashboard/location")

        ref.setValue(location).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    override fun uploadImage(imageUri: Uri, callback: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("users/$userId/dashboard_image.jpg")

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    database.getReference("users/$userId/dashboard/imageUrl").setValue(uri.toString())
                    callback(uri.toString())
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
