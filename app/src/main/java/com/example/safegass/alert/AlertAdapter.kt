package com.example.safegass.alert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R

class AlertAdapter(private var alerts: List<Alert>) :
    RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtMessage: TextView? = itemView.findViewById(R.id.txtMessage)
        private val textAlertTitle: TextView? = itemView.findViewById(R.id.textAlertTitle)
        private val txtDescription: TextView? = itemView.findViewById(R.id.txtDescription)
        private val txtLocation: TextView? = itemView.findViewById(R.id.txtLocation)
        private val txtTime: TextView? = itemView.findViewById(R.id.txtTime)

        fun bind(alert: Alert) {
            txtMessage?.text = alert.type
            textAlertTitle?.text = alert.title
            txtDescription?.text = alert.description
            txtLocation?.text = alert.source

            // ✅ Convert Long -> readable time (Firebase stores seconds)
            val formattedTime = try {
                val date = java.util.Date(alert.time * 1000) // convert seconds → ms
                val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                sdf.format(date)
            } catch (e: Exception) {
                alert.time.toString()
            }

            txtTime?.text = formattedTime
        }
    } // ← this brace was missing in your version!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(alerts[position])
    }

    override fun getItemCount(): Int = alerts.size

    fun updateAlerts(newAlerts: List<Alert>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }
}
