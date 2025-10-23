package com.example.safegass.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.safegass.R
import com.example.safegass.edit.EditProfileActivity
import com.example.safegass.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class ProfilePageActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter

    private lateinit var userName: TextView
    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var profileImage: ImageView
    private lateinit var btnUploadImage: ImageButton
    private lateinit var btnEditProfile: Button
    private lateinit var btnMenu: ImageView

    private lateinit var textPhone: TextView

    private val storageRef = FirebaseStorage.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                if (imageUri != null) uploadProfileImage(imageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        // Initialize views
        userName = findViewById(R.id.userName)
        textName = findViewById(R.id.textName)
        textEmail = findViewById(R.id.textEmail)
        profileImage = findViewById(R.id.profileImage)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnMenu = findViewById(R.id.btnMenu)
        textPhone = findViewById(R.id.textPhone)


        // Initialize presenter
        presenter = ProfilePresenter(this, ProfileModel())
        presenter.loadProfile()

        // Menu button → SettingsActivity
        btnMenu.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Edit Profile button → EditProfileActivity
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Upload image
        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }
    }

    private fun uploadProfileImage(imageUri: Uri) {
        if (userId == null) return
        val imageRef = storageRef.child("profile_images/$userId.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    presenter.saveProfileImageUrl(uri.toString())
                    Glide.with(this).load(uri).into(profileImage)
                    Toast.makeText(this, "Profile image updated!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun showProfile(profile: UserProfile) {
        val fullName = "${profile.firstName} ${profile.lastName}"
        userName.text = fullName
        textName.text = fullName
        textEmail.text = profile.email
        textPhone.text = profile.phone   // ✅ Add this line

        if (!profile.imageUrl.isNullOrEmpty()) {
            Glide.with(this).load(profile.imageUrl).into(profileImage)
        }
    }


    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}