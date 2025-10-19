package com.example.safegass.dashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.safegass.R

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var imageHouseGas: ImageView
    private lateinit var buttonUploadImage: ImageButton
    private lateinit var buttonSaveLocation: Button
    private lateinit var editLocationInput: EditText
    private lateinit var textPPM: TextView
    private lateinit var textStatus: TextView
    private lateinit var textLocation: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var presenter: DashboardPresenter
    private var imageUrl: String? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        imageHouseGas = findViewById(R.id.imageHouseGas)
        buttonUploadImage = findViewById(R.id.buttonUploadImage)
        buttonSaveLocation = findViewById(R.id.buttonSaveLocation)
        editLocationInput = findViewById(R.id.editLocationInput)
        textPPM = findViewById(R.id.textPPM)
        textStatus = findViewById(R.id.textStatus)
        textLocation = findViewById(R.id.textLocation)
        progressBar = ProgressBar(this)

        presenter = DashboardPresenter(this, DashboardRepository())
        presenter.loadDashboardData()

        buttonUploadImage.setOnClickListener { pickImage() }
        buttonSaveLocation.setOnClickListener {
            val location = editLocationInput.text.toString()
            presenter.saveLocation(location, imageUrl)
            Toast.makeText(this, "Location saved!", Toast.LENGTH_SHORT).show()
        }
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

    override fun showDashboardData(data: DashboardData) {
        textPPM.text = data.ppm
        textStatus.text = data.status
        textLocation.text = data.location
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
        Toast.makeText(this, "Image uploaded!", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = ProgressBar.VISIBLE
        else progressBar.visibility = ProgressBar.GONE
    }
}
