package com.example.safegass.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.safegass.R
import com.example.safegass.dashboard.DashboardActivity

class ViewPageActivity : AppCompatActivity(), ViewPageContract.View {

    private lateinit var presenter: ViewPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details) // XML you shared

        presenter = ViewPagePresenter(this)

        val btnBack: ImageView = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            presenter.onBackButtonClicked()
        }
    }

    override fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
}
