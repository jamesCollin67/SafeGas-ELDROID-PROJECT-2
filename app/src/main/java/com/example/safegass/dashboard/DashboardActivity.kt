package com.example.safegass.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.history.HistoryActivity
import com.example.safegass.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardContract.Presenter

    // UI Components
    private lateinit var imageHouseGas: ImageView
    private lateinit var textPPM: TextView
    private lateinit var textStatus: TextView
    private lateinit var textLocation: TextView
    private lateinit var textLastUpdated: TextView
    private lateinit var textActiveAlerts: TextView
    private lateinit var textOnlineDevices: TextView
    private lateinit var textAveragePPM: TextView
    private lateinit var textPeakPPM: TextView
    private lateinit var buttonSaveLocation: Button
    private lateinit var editLocationInput: EditText
    private lateinit var buttonUploadImage: ImageButton

    // Bottom Navigation
    private lateinit var navAlerts: LinearLayout
    private lateinit var navHistory: LinearLayout
    private lateinit var navSettings: LinearLayout

    private val IMAGE_PICK_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this, DashboardRepository())

        initViews()
        setupListeners()

        presenter.loadDashboardData()
    }

    private fun initViews() {
        imageHouseGas = findViewById(R.id.imageHouseGas)
        textPPM = findViewById(R.id.textPPM)
        textStatus = findViewById(R.id.textStatus)
        textLocation = findViewById(R.id.textLocation)
        textLastUpdated = findViewById(R.id.textLastUpdated)
        textActiveAlerts = findViewById(R.id.textActiveAlerts)
        textOnlineDevices = findViewById(R.id.textOnlineDevices)
        textAveragePPM = findViewById(R.id.textAveragePPM)
        textPeakPPM = findViewById(R.id.textPeakPPM)
        buttonSaveLocation = findViewById(R.id.buttonSaveLocation)
        editLocationInput = findViewById(R.id.editLocationInput)
        buttonUploadImage = findViewById(R.id.buttonUploadImage)

        navAlerts = findViewById(R.id.navAlerts)
        navHistory = findViewById(R.id.navHistory)
        navSettings = findViewById(R.id.navSettings)
    }

    private fun setupListeners() {
        buttonSaveLocation.setOnClickListener {
            val location = editLocationInput.text.toString().trim()
            presenter.saveLocation(location)
        }

        buttonUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
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
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                presenter.uploadImage(uri)
            }
        }
    }

    // ==== View Implementation ====

    override fun showImage(imageUrl: String) {
        if (imageUrl.isNotBlank()) {
            Glide.with(this).load(imageUrl).into(imageHouseGas)
        }
    }

    override fun showPPM(ppm: Int) {
        textPPM.text = if (ppm == 0) "No data yet" else "$ppm ppm"
    }

    override fun showStatus(status: String) {
        textStatus.text = status

        // Change text color based on gas safety status
        when (status) {
            "Safe" -> {
                textStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            }
            "Warning" -> {
                textStatus.setTextColor(getColor(android.R.color.holo_orange_light))
            }
            "Danger" -> {
                textStatus.setTextColor(getColor(android.R.color.holo_red_dark))
            }
            else -> {
                textStatus.setTextColor(getColor(android.R.color.darker_gray))
            }
        }
    }


    override fun showLocation(location: String) {
        // 👇 Show the saved location name in the label only (not EditText)
        textLocation.text = if (location.isBlank()) "Please save a location" else location

        // 👇 Make sure the EditText stays empty after saving
        // (prevents the location name from reappearing in the input box)
        editLocationInput.text.clear()
    }



    override fun showLastUpdated(time: String) {
        textLastUpdated.text = "Last updated: $time"
    }

    override fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPPM: Int, peakPPM: Int) {
        textActiveAlerts.text = activeAlerts.toString()
        textOnlineDevices.text = onlineDevices.toString()
        textAveragePPM.text = "$avgPPM ppm"
        textPeakPPM.text = "$peakPPM ppm"
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
    }
    override fun clearLocationInput() {
        editLocationInput.text.clear()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }
}
