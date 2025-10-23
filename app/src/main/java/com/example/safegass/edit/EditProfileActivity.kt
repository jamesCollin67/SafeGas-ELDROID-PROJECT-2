package com.example.safegass.edit

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.profile.UserProfile
import com.example.safegass.profile.ProfilePageActivity

class EditProfileActivity : AppCompatActivity(), EditProfileContract.View {

    private lateinit var presenter: EditProfileContract.Presenter

    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPhone: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)

        // Initialize views
        inputName = findViewById(R.id.inputName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPhone = findViewById(R.id.inputPhone)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        backButton = findViewById(R.id.backButton)

        // Initialize presenter
        presenter = EditProfilePresenter(this, EditProfileModel())
        presenter.loadProfile()

        // Save button
        btnSaveChanges.setOnClickListener {
            val name = inputName.text.toString()
            val email = inputEmail.text.toString()
            val phone = inputPhone.text.toString()
            presenter.saveProfileChanges(name, email, phone)
        }

        // Back button → SettingsActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // ==== Contract Implementation ====

    override fun showProfile(profile: UserProfile) {
        val fullName = "${profile.firstName} ${profile.lastName}".trim()
        inputName.setText(fullName)
        inputEmail.setText(profile.email)
        inputPhone.setText(profile.phone)
    }

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
