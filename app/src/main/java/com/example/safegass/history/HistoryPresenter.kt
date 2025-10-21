package com.example.safegass.history

import java.text.SimpleDateFormat
import java.util.*

class HistoryPresenter(private val view: HistoryContract.View) : HistoryContract.Presenter {

    private val model = HistoryModel()
    private var allRecords: List<HistoryRecord> = listOf()

    override fun loadHistory() {
        model.fetchHistory(
            callback = { records ->
                allRecords = records.sortedByDescending { it.timestamp }
                view.showHistory(allRecords)
            },
            onError = { message -> view.showError(message) }
        )
    }

    override fun applyFilters(from: String, to: String, location: String) {
        val filtered = allRecords.filter { record ->
            val matchLocation = if (location.isNotEmpty())
                record.location.contains(location, ignoreCase = true)
            else true

            val matchDate = if (from.isNotEmpty() && to.isNotEmpty()) {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val fromDate = sdf.parse(from)
                    val toDate = sdf.parse(to)
                    val recordDate = sdf.parse(record.timestamp.substring(0, 10))
                    recordDate != null && fromDate != null && toDate != null &&
                            !recordDate.before(fromDate) && !recordDate.after(toDate)
                } catch (e: Exception) {
                    true
                }
            } else true

            matchLocation && matchDate
        }

        view.showHistory(filtered)
    }

    override fun resetFilters() {
        view.showHistory(allRecords)
    }

    override fun onDashboardClicked() = view.navigateToDashboard()
    override fun onAlertClicked() = view.navigateToAlert()
    override fun onSettingsClicked() = view.navigateToSettings()
}
