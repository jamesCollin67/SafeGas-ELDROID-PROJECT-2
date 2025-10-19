package com.example.safegass.dashboard

import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val repository: DashboardRepository
) : DashboardContract.Presenter {

    private val db = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference

    private var dashboardListener: ValueEventListener? = null
    private var overviewListener: ValueEventListener? = null

    override fun loadDashboardData() {
        view.showLoading(true)

        val dashboardRef = db.child("dashboard")
        val overviewRef = db.child("overview")

        // 🔁 Listen continuously for changes in dashboard
        dashboardListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ppm = snapshot.child("ppm").getValue(String::class.java) ?: "0 ppm"
                val status = snapshot.child("status").getValue(String::class.java) ?: "Unknown"
                val location = snapshot.child("location").getValue(String::class.java) ?: "Unknown"
                val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                // 🔁 Also listen continuously for overview changes
                if (overviewListener == null) {
                    overviewListener = object : ValueEventListener {
                        override fun onDataChange(overviewSnap: DataSnapshot) {
                            val activeAlerts = overviewSnap.child("activeAlerts").getValue(Int::class.java) ?: 0
                            val onlineDevices = overviewSnap.child("onlineDevices").getValue(Int::class.java) ?: 0
                            val avgPpm = "${overviewSnap.child("averagePPM").getValue(Int::class.java) ?: 0} ppm"
                            val peakPpm = "${overviewSnap.child("peakPPM").getValue(Int::class.java) ?: 0} ppm"

                            val lastUpdated = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                            val data = DashboardData(
                                ppm = ppm,
                                status = status,
                                location = location,
                                imageUrl = imageUrl,
                                lastUpdated = lastUpdated,
                                activeAlerts = activeAlerts,
                                onlineDevices = onlineDevices,
                                avgPpm = avgPpm,
                                peakPpm = peakPpm
                            )
                            view.showDashboardData(data)
                            view.showLoading(false)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            view.showError("Failed to load overview: ${error.message}")
                            view.showLoading(false)
                        }
                    }
                    overviewRef.addValueEventListener(overviewListener!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                view.showError("Failed to load dashboard: ${error.message}")
                view.showLoading(false)
            }
        }

        dashboardRef.addValueEventListener(dashboardListener!!)
    }

    override fun uploadImage(imageUri: Uri) {
        view.showLoading(true)
        val fileName = "dashboard/${System.currentTimeMillis()}.jpg"
        val ref = storage.child(fileName)

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    db.child("dashboard").child("imageUrl").setValue(url)
                    view.showUploadSuccess(url)
                    view.showLoading(false)
                }
            }
            .addOnFailureListener {
                view.showError("Upload failed: ${it.message}")
                view.showLoading(false)
            }
    }

    override fun saveLocation(location: String, imageUrl: String?) {
        val updates = mapOf(
            "location" to location,
            "imageUrl" to (imageUrl ?: "")
        )
        db.child("dashboard").updateChildren(updates)
            .addOnSuccessListener {
                // ✅ Immediately reflect the new location in the UI
                val currentData = DashboardData(
                    location = location,
                    imageUrl = imageUrl
                )
                view.showDashboardData(currentData)
            }
            .addOnFailureListener {
                view.showError("Failed to save location: ${it.message}")
            }
    }

    fun removeListeners() {
        dashboardListener?.let { db.child("dashboard").removeEventListener(it) }
        overviewListener?.let { db.child("overview").removeEventListener(it) }
    }
}
