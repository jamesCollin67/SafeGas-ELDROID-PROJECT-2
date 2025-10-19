package com.example.safegass.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.settings.SettingsActivity
import com.google.firebase.database.*

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    // UI components
    private lateinit var imageHouseGas: ImageView
    private lateinit var buttonUploadImage: ImageButton
    private lateinit var buttonSaveLocation: Button
    private lateinit var editLocationInput: EditText
    private lateinit var textPPM: TextView
    private lateinit var textStatus: TextView
    private lateinit var textLocation: TextView

    private lateinit var textActiveAlerts: TextView
    private lateinit var textOnlineDevices: TextView
    private lateinit var textAveragePPM: TextView
    private lateinit var textPeakPPM: TextView
    private lateinit var textLastUpdated: TextView

    private lateinit var navDashboard: LinearLayout
    private lateinit var navAlerts: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    private lateinit var presenter: DashboardPresenter
    private var imageUrl: String? = null
    private lateinit var dashboardRef: DatabaseReference

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 🔗 Bind all UI components
        imageHouseGas = findViewById(R.id.imageHouseGas)
        buttonUploadImage = findViewById(R.id.buttonUploadImage)
        buttonSaveLocation = findViewById(R.id.buttonSaveLocation)
        editLocationInput = findViewById(R.id.editLocationInput)
        textPPM = findViewById(R.id.textPPM)
        textStatus = findViewById(R.id.textStatus)
        textLocation = findViewById(R.id.textLocation)
        textActiveAlerts = findViewById(R.id.textActiveAlerts)
        textOnlineDevices = findViewById(R.id.textOnlineDevices)
        textAveragePPM = findViewById(R.id.textAveragePPM)
        textPeakPPM = findViewById(R.id.textPeakPPM)
        textLastUpdated = findViewById(R.id.textLastUpdated)

        navDashboard = findViewById(R.id.navDashboard)
        navAlerts = findViewById(R.id.navAlerts)
        navHistory = findViewById(R.id.navHistory)
        navSettings = findViewById(R.id.navSettings)

        // 🎯 Initialize presenter and Firebase reference
        presenter = DashboardPresenter(this, DashboardRepository())
        dashboardRef = FirebaseDatabase.getInstance().getReference("dashboard")

        // Default text placeholders
        textPPM.text = "Waiting for data..."
        textStatus.text = "Waiting for sensor..."
        textLocation.text = "Need to save location"

        // Load static data (overview, etc.)
        presenter.loadDashboardData()

        // 🔁 Real-time updates from Firebase dashboard node
        dashboardRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ppm = snapshot.child("ppm").getValue(String::class.java) ?: "0 ppm"
                val status = snapshot.child("status").getValue(String::class.java) ?: "Unknown"
                val location = snapshot.child("location").getValue(String::class.java) ?: "Need to save location"

                textPPM.text = ppm
                textStatus.text = status
                textLocation.text = location

                // Update last updated time
                val currentTime = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
                textLastUpdated.text = "Last updated $currentTime"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DashboardActivity, "Failed to get updates", Toast.LENGTH_SHORT).show()
            }
        })

        // 📸 Upload image button
        buttonUploadImage.setOnClickListener { pickImage() }

        // 💾 Save location button
        buttonSaveLocation.setOnClickListener {
            val location = editLocationInput.text.toString().trim()
            if (location.isEmpty()) {
                Toast.makeText(this, "Please enter a location name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            textLocation.text = "Saving..."
            presenter.saveLocation(location, imageUrl)

            // also push location to Firebase dashboard node
            dashboardRef.child("location").setValue(location)
                .addOnSuccessListener {
                    textLocation.text = location
                    Toast.makeText(this, "Location saved!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    textLocation.text = "Failed to save location"
                    Toast.makeText(this, "Error saving location", Toast.LENGTH_SHORT).show()
                }
        }

        // ⚙️ Bottom navigation setup
        setupBottomNavigation()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data?.data != null) {
            val uri = data.data!!
            presenter.uploadImage(uri)
        }
    }

    // --- Presenter Callbacks ---
    override fun showDashboardData(data: DashboardData) {
        textPPM.text = data.ppm.ifEmpty { "Waiting for data..." }
        textStatus.text = data.status.ifEmpty { "Waiting for sensor..." }
        textLocation.text = data.location.ifEmpty { "Need to save location" }

        textActiveAlerts.text = data.activeAlerts.toString()
        textOnlineDevices.text = data.onlineDevices.toString()
        textAveragePPM.text = data.avgPpm
        textPeakPPM.text = data.peakPpm
        textLastUpdated.text = "Last updated ${data.lastUpdated}"

        imageUrl = data.imageUrl
        if (!data.imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(data.imageUrl)
                .into(imageHouseGas)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showUploadSuccess(imageUrl: String) {
        this.imageUrl = imageUrl
        Glide.with(this).load(imageUrl).into(imageHouseGas)
        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(isLoading: Boolean) {
        // Optional: could show/hide a progress bar
    }

    private fun setupBottomNavigation() {
        navDashboard.setOnClickListener { }
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
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.removeListeners()
    }
}
