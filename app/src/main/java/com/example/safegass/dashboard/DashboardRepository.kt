package com.example.safegass.dashboard

import com.google.firebase.database.*

class DashboardRepository : DashboardContract.Repository {

    private val dbRef: DatabaseReference =
        FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
            .reference

    private var dashboardListener: ValueEventListener? = null
    private var sensorListener: ValueEventListener? = null
    private var overviewListener: ValueEventListener? = null

    // keep latest snapshots for combine
    private var sensorSnap: DataSnapshot? = null
    private var overviewSnap: DataSnapshot? = null

    override fun addListener(callback: DashboardContract.RepositoryCallback) {
        // 1) Primary: listen to "dashboard" node (single object)
        val dashRef = dbRef.child("dashboard")
        dashboardListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) return
                try {
                    val data = parseDashboardSnapshot(snapshot)
                    callback.onData(data)
                } catch (t: Throwable) {
                    callback.onError("Parsing dashboard node failed: ${t.message}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        }
        dashRef.addValueEventListener(dashboardListener as ValueEventListener)

        // 2) Also attach sensorData + overview listeners so repository works if DB uses split nodes
        val sensorRef = dbRef.child("sensorData")
        sensorListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sensorSnap = snapshot
                combineAndEmitIfReady(callback)
            }
            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        }
        sensorRef.addValueEventListener(sensorListener as ValueEventListener)

        val overviewRef = dbRef.child("overview")
        overviewListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                overviewSnap = snapshot
                combineAndEmitIfReady(callback)
            }
            override fun onCancelled(error: DatabaseError) {
                callback.onError(error.message)
            }
        }
        overviewRef.addValueEventListener(overviewListener as ValueEventListener)
    }

    override fun removeListener() {
        try {
            dashboardListener?.let { dbRef.child("dashboard").removeEventListener(it) }
            sensorListener?.let { dbRef.child("sensorData").removeEventListener(it) }
            overviewListener?.let { dbRef.child("overview").removeEventListener(it) }
        } catch (_: Exception) { }
        dashboardListener = null
        sensorListener = null
        overviewListener = null
        sensorSnap = null
        overviewSnap = null
    }

    // parse when there's a single "dashboard" node
    private fun parseDashboardSnapshot(snapshot: DataSnapshot): DashboardData {
        val ppm = anyToPpmString(snapshot.child("ppm").value)
        val status = snapshot.child("status").getValue(String::class.java) ?: deriveStatusFromPpm(ppm)
        val location = snapshot.child("location").getValue(String::class.java) ?: "Unknown"
        val lastUpdated = snapshot.child("lastUpdated").getValue(String::class.java) ?: ""
        val activeAlerts = snapshot.child("activeAlerts").getValue(Int::class.java) ?: 0
        val onlineDevices = snapshot.child("onlineDevices").getValue(Int::class.java) ?: 0
        val avg = anyToPpmString(snapshot.child("averagePPM").value)
        val peak = anyToPpmString(snapshot.child("peakPPM").value)

        return DashboardData(ppm, status, location, lastUpdated, activeAlerts, onlineDevices, avg, peak)
    }

    // if DB uses sensorData + overview nodes, combine them here
    private fun combineAndEmitIfReady(callback: DashboardContract.RepositoryCallback) {
        // only combine when at least one snapshot exists (we give best-effort defaults)
        val s = sensorSnap
        val o = overviewSnap

        // Extract from sensorData (if present)
        val ppm = s?.child("ppm")?.value ?: s?.child("gasLevel")?.value ?: s?.child("ppm")?.value
        val location = s?.child("location")?.getValue(String::class.java) ?: "Unknown"
        val lastUpdated = s?.child("lastUpdated")?.getValue(String::class.java) ?: ""

        // Extract from overview (if present)
        val activeAlerts = o?.child("activeAlerts")?.getValue(Int::class.java) ?: 0
        val onlineDevices = o?.child("onlineDevices")?.getValue(Int::class.java) ?: 0
        val avg = o?.child("averagePPM")?.value ?: o?.child("avgPpm")?.value
        val peak = o?.child("peakPPM")?.value ?: o?.child("peakPpm")?.value

        val ppmStr = anyToPpmString(ppm)
        val avgStr = anyToPpmString(avg)
        val peakStr = anyToPpmString(peak)
        val statusStr = deriveStatusFromPpm(ppmStr)

        val data = DashboardData(
            ppm = ppmStr,
            status = statusStr,
            location = location,
            lastUpdated = lastUpdated,
            activeAlerts = activeAlerts,
            onlineDevices = onlineDevices,
            avgPpm = avgStr,
            peakPpm = peakStr
        )
        callback.onData(data)
    }

    // helper to produce "NNN ppm" regardless of number/string
    private fun anyToPpmString(value: Any?): String {
        return when (value) {
            null -> "0 ppm"
            is Long -> "${value} ppm"
            is Int -> "${value} ppm"
            is Double -> "${value.toInt()} ppm"
            is String -> {
                if (value.contains("ppm", ignoreCase = true)) value
                else {
                    val digits = value.filter { it.isDigit() }
                    if (digits.isNotEmpty()) "$digits ppm" else value
                }
            }
            else -> "0 ppm"
        }
    }

    private fun deriveStatusFromPpm(ppmString: String): String {
        val num = ppmString.filter { it.isDigit() }.toIntOrNull() ?: 0
        return when {
            num <= 500 -> "Safe"
            num <= 1000 -> "Warning"
            else -> "Danger"
        }
    }
}
