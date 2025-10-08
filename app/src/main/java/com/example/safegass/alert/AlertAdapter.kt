package com.example.safegass.alert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R

class AlertAdapter(
    private var alerts: List<Alert>,
    private val listener: AlertActionListener
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    interface AlertActionListener {
        fun onMuteClicked(alertId: Int)
        fun onCallEmergencyClicked(alertId: Int)
    }

    inner class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.alertTitle)
        val description: TextView = itemView.findViewById(R.id.alertDescription)
        val source: TextView = itemView.findViewById(R.id.alertSource)
        val timeAgo: TextView = itemView.findViewById(R.id.alertTime)
        val btnMute: Button = itemView.findViewById(R.id.btnMuteAlarm)
        val btnCall: Button = itemView.findViewById(R.id.btnCallEmergency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        holder.title.text = alert.title
        holder.description.text = alert.description
        holder.source.text = alert.source
        holder.timeAgo.text = alert.timeAgo

        holder.btnMute.setOnClickListener { listener.onMuteClicked(alert.id) }
        holder.btnCall.setOnClickListener { listener.onCallEmergencyClicked(alert.id) }
    }

    override fun getItemCount(): Int = alerts.size

    fun updateAlerts(newAlerts: List<Alert>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }
}
