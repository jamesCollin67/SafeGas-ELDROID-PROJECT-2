package com.example.safegass.history

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R
import com.example.safegass.alert.AlertActivity
import com.example.safegass.dashboard.DashboardActivity
import com.example.safegass.settings.SettingsActivity

class HistoryActivity : AppCompatActivity(), HistoryContract.View {

    private lateinit var presenter: HistoryPresenter
    private lateinit var adapter: HistoryAdapter

    private lateinit var inputFrom: EditText
    private lateinit var inputTo: EditText
    private lateinit var inputAll: EditText
    private lateinit var recycler: RecyclerView
    private lateinit var btnApply: Button
    private lateinit var btnReset: Button
    private lateinit var txtLastSync: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        presenter = HistoryPresenter(this)

        inputFrom = findViewById(R.id.inputFrom)
        inputTo = findViewById(R.id.inputTo)
        inputAll = findViewById(R.id.inputAll)
        recycler = findViewById(R.id.recyclerAlerts)
        btnApply = findViewById(R.id.btnApply)
        btnReset = findViewById(R.id.btnReset)
        txtLastSync = findViewById(R.id.txtLastSync)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(emptyList())
        recycler.adapter = adapter

        btnApply.setOnClickListener {
            presenter.applyFilters(
                inputFrom.text.toString(),
                inputTo.text.toString(),
                inputAll.text.toString()
            )
        }

        btnReset.setOnClickListener {
            inputFrom.text.clear()
            inputTo.text.clear()
            inputAll.text.clear()
            presenter.resetFilters()
        }

        presenter.loadHistory()

        findViewById<LinearLayout>(R.id.navDashboard).setOnClickListener {
            presenter.onDashboardClicked()
        }
        findViewById<LinearLayout>(R.id.navAlerts).setOnClickListener {
            presenter.onAlertClicked()
        }
        findViewById<LinearLayout>(R.id.navSettings).setOnClickListener {
            presenter.onSettingsClicked()
        }
    }

    override fun showHistory(records: List<HistoryRecord>) {
        adapter.updateData(records)
        txtLastSync.text = "Last sync: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}"
    }

    override fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    override fun navigateToAlert() {
        startActivity(Intent(this, AlertActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    override fun navigateToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }
}
