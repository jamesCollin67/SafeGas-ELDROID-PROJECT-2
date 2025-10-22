package com.example.safegass.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.dashboard.DashboardActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.login.LoginActivity
import com.example.safegass.connected.ConnectedDevicesActivity
import com.example.safegass.profile.ProfilePageActivity
import com.example.safegass.update.UpdatePasswordActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SettingsActivity : Activity(), SettingsContract.View {

    private lateinit var presenter: SettingsPresenter
    private lateinit var inputLanguage: EditText
    private lateinit var textLastSync: TextView
    private lateinit var btnApplyLanguage: Button
    private lateinit var btnManualSync: Button
    private lateinit var btnLogout: Button
    private lateinit var btnConnectedDevices: TextView
    private lateinit var textConnectedDevices: TextView
    private lateinit var btnUpdatesPasswords: Button
    private lateinit var btnViewProfile: Button

    private lateinit var dbRef: DatabaseReference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        presenter = SettingsPresenter(this, this)

        // === Bind views ===
        inputLanguage = findViewById(R.id.inputLanguage)
        textLastSync = findViewById(R.id.textLastSync)
        btnApplyLanguage = findViewById(R.id.btnApplyLanguage)
        btnManualSync = findViewById(R.id.btnManualSync)
        btnLogout = findViewById(R.id.btnLogout)
        btnConnectedDevices = findViewById(R.id.btnConnectedDevices)
        btnUpdatesPasswords = findViewById(R.id.btnUpdatesPassword)
        btnViewProfile = findViewById(R.id.btnViewProfile)
        textConnectedDevices = findViewById(R.id.textConnectedDevices)

        // === Firebase Reference ===
        val uid = auth.currentUser?.uid
        if (uid != null) {
            dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("devices")
            loadConnectedDevices()
        } else {
            textConnectedDevices.text = "Not logged in"
        }

        // === Load initial settings ===
        presenter.loadSettings()

        // === Button listeners ===
        btnApplyLanguage.setOnClickListener {
            presenter.applyLanguage(inputLanguage.text.toString())
        }

        btnManualSync.setOnClickListener {
            presenter.manualSync()
        }

        btnLogout.setOnClickListener {
            presenter.logout()
        }

        // === Navigate to Connected Devices ===
        btnConnectedDevices.setOnClickListener {
            startActivity(Intent(this, ConnectedDevicesActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // === Navigate to Profile Page ===
        btnViewProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePageActivity::class.java))
            overridePendingTransition(0, 0)
        }

        btnUpdatesPasswords.setOnClickListener {
            startActivity(Intent(this, UpdatePasswordActivity::class.java))
            overridePendingTransition(0, 0)
        }

        setupNavigationBar()
    }

    // 🔹 Real-time load of connected devices with nice formatting
    private fun loadConnectedDevices() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val devicesDisplay = StringBuilder()

                for (deviceSnap in snapshot.children) {
                    val location = deviceSnap.child("location").getValue(String::class.java) ?: "Unknown"
                    val serial = deviceSnap.child("serial").getValue(String::class.java) ?: "No Serial"

                    // Format output like:
                    // kwarto Sensor (ESP32-KWARTO)
                    // Rename | Location | Remove
                    // Battery 82%, Last seen 2 min ago
                    devicesDisplay.append("$location Sensor ($serial)\n")
                    devicesDisplay.append("Rename | Location | Remove\n")
                    devicesDisplay.append("Battery 82%, Last seen 2 min ago\n\n")
                }

                textConnectedDevices.text = if (devicesDisplay.isEmpty()) {
                    "No connected devices"
                } else {
                    devicesDisplay.toString().trim()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                textConnectedDevices.text = "Failed to load devices: ${error.message}"
            }
        })
    }

    private fun setupNavigationBar() {
        val navDashboard = findViewById<LinearLayout>(R.id.navDashboard)
        val navAlerts = findViewById<LinearLayout>(R.id.navAlerts)
        val navHistory = findViewById<LinearLayout>(R.id.navHistory)
        val navSettings = findViewById<LinearLayout>(R.id.navSettings)

        navDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navAlerts.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        navSettings.setOnClickListener {
            // already here
        }
    }

    // === MVP View Implementations ===
    override fun showLastSync(time: String) {
        textLastSync.text = time
    }

    override fun showLanguage(language: String) {
        inputLanguage.setText(language)
    }

    override fun showLogoutSuccess() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
