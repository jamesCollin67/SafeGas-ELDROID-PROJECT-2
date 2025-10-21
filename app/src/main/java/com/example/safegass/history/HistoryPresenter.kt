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
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val fromDate = if (from.isNotEmpty()) sdf.parse(from) else null
        val toDate = if (to.isNotEmpty()) sdf.parse(to) else null

        val filtered = allRecords.filter { record ->
            val matchLocation = if (location.isNotEmpty())
                record.location.contains(location, ignoreCase = true)
            else true

            val recordDate = try {
                sdf.parse(record.timestamp.substring(0, 10))
            } catch (e: Exception) {
                null
            }

            val matchDate = if (fromDate != null && toDate != null && recordDate != null) {
                !recordDate.before(fromDate) && !recordDate.after(toDate)
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

    // === Helper function to handle any timestamp format ===
    private fun parseDateFlexible(dateStr: String?): Date? {
        if (dateStr.isNullOrBlank()) return null

        val possibleFormats = listOf(
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "MM/dd/yyyy",
            "dd-MM-yyyy",
            "dd/MM/yyyy"
        )

        for (format in possibleFormats) {
            try {
                return SimpleDateFormat(format, Locale.getDefault()).parse(dateStr)
            } catch (_: Exception) { }
        }

        try {
            val trimmed = dateStr.substring(0, 10)
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(trimmed)
        } catch (_: Exception) { }

        return null
    }
}
