package com.example.safegass.settings

import android.app.Activity
import androidx.appcompat.app.AlertDialog
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
    private lateinit var textUserName: TextView
    private lateinit var textUserEmail: TextView
    private lateinit var databaseRef: DatabaseReference

    private lateinit var btnConnectedDevices: TextView
    private lateinit var textConnectedDevices: TextView
    private lateinit var btnUpdatesPasswords: Button
    private lateinit var btnViewProfile: Button
    private lateinit var connectedDevicesRef: DatabaseReference
    private lateinit var btnDeactivateAccount: Button



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
        textUserName = findViewById(R.id.textUserName)
        textUserEmail = findViewById(R.id.textUserEmail)
        btnDeactivateAccount = findViewById(R.id.btnDeactivateAccount)

        val currentUser = FirebaseAuth.getInstance().currentUser
        databaseRef = FirebaseDatabase.getInstance().getReference("users")

        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = databaseRef.child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstname = snapshot.child("firstName").getValue(String::class.java)
                    val lastname = snapshot.child("lastName").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)

                    val fullName = listOfNotNull(firstname, lastname).joinToString(" ").trim()
                    textUserName.text = if (fullName.isNotBlank()) fullName else "Unknown User"
                    textUserEmail.text = email ?: "No Email Found"
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SettingsActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            })
        }




        // === Firebase Reference ===
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        connectedDevicesRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("connectedDevices")

        loadConnectedDevices()


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
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Log out of your account?")
            builder.setMessage("Are you sure you want to log out?")

            builder.setPositiveButton("Log out") { dialog, _ ->
                presenter.logout()
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

            // Optional: style the buttons (to make Cancel gray and Log out red)
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(android.graphics.Color.RED)
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.setTextColor(android.graphics.Color.GRAY)
        }
        btnDeactivateAccount.setOnClickListener {
            showDeactivationDialog()
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
        val devicesDisplay = findViewById<TextView>(R.id.textConnectedDevices) // change to your TextView ID
        connectedDevicesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val sb = StringBuilder()
                    for (deviceSnap in snapshot.children) {
                        val serial = deviceSnap.child("serial").getValue(String::class.java) ?: "Unknown"
                        val location = deviceSnap.child("location").getValue(String::class.java) ?: "N/A"
                        val status = deviceSnap.child("status").getValue(String::class.java) ?: "Unknown"
                        sb.append("Device: $serial\nLocation: $location\nStatus: $status\n\n")
                    }
                    devicesDisplay.text = sb.toString()
                } else {
                    devicesDisplay.text = "No connected devices found"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                devicesDisplay.text = "Failed to load devices: ${error.message}"
            }
        })
    }
    override fun showDeactivationDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Deactivate Account")
        builder.setMessage("Are you sure you want to deactivate your account? You can reactivate it by logging in again.")

        builder.setPositiveButton("Deactivate") { dialog, _ ->
            presenter.deactivateAccount()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)?.setTextColor(android.graphics.Color.RED)
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.setTextColor(android.graphics.Color.GRAY)
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
