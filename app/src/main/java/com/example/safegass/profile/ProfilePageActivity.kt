package com.example.safegass.profile

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.edit.EditProfileActivity

class ProfilePageActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter

    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var textPhone: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnMenu: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        // Initialize views
        textName = findViewById(R.id.textName)
        textEmail = findViewById(R.id.textEmail)
        textPhone = findViewById(R.id.textPhone)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnMenu = findViewById(R.id.btnMenu)

        // Setup Presenter
        presenter = ProfilePresenter(this, ProfileModel())
        presenter.loadProfile()

        // Navigation
        btnMenu.setOnClickListener {
            onBackPressed()
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showProfile(profile: UserProfile) {
        textName.text = profile.name
        textEmail.text = profile.email
        textPhone.text = profile.phone
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
