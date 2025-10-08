package com.example.safegass.edit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.profile.UserProfile

class EditProfileActivity : AppCompatActivity(), EditProfileContract.View {

    private lateinit var presenter: EditProfileContract.Presenter

    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPhone: EditText
    private lateinit var inputAddress: EditText
    private lateinit var inputCity: EditText
    private lateinit var inputState: EditText
    private lateinit var inputZip: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)

        // Initialize Views
        inputName = findViewById(R.id.inputName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPhone = findViewById(R.id.inputPhone)
        inputAddress = findViewById(R.id.inputAddress)
        inputCity = findViewById(R.id.inputCity)
        inputState = findViewById(R.id.inputState)
        inputZip = findViewById(R.id.inputZip)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        backButton = findViewById(R.id.backButton)

        // Initialize MVP
        presenter = EditProfilePresenter(this, EditProfileModel())
        presenter.loadProfile()

        // Save Button
        btnSaveChanges.setOnClickListener {
            val updatedProfile = UserProfile(
                name = inputName.text.toString(),
                email = inputEmail.text.toString(),
                phone = inputPhone.text.toString(),
                address = inputAddress.text.toString(),
                city = inputCity.text.toString(),
                state = inputState.text.toString(),
                zipCode = inputZip.text.toString()
            )

            presenter.saveProfile(updatedProfile)
        }

        // Back Button
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun showProfile(profile: UserProfile) {
        inputName.setText(profile.name)
        inputEmail.setText(profile.email)
        inputPhone.setText(profile.phone)
        inputAddress.setText(profile.address)
        inputCity.setText(profile.city)
        inputState.setText(profile.state)
        inputZip.setText(profile.zipCode)
    }

    override fun showSaveSuccess() {
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
