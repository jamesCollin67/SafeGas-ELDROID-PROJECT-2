package com.example.safegass.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R

class HistoryAdapter(private var list: List<HistoryRecord>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.imgLeakIcon)
        val title: TextView = view.findViewById(R.id.txtLeakTitle)
        val status: TextView = view.findViewById(R.id.txtLeakStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leak_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = list[position]
        holder.title.text = "${record.ppm} ppm - ${record.location.ifEmpty { "Unknown" }}"
        holder.status.text = "${record.status}, ${record.timestamp}"

        when (record.status.lowercase()) {
            "safe" -> holder.icon.setImageResource(R.drawable.ic_safe)
            "warning" -> holder.icon.setImageResource(R.drawable.ic_warning)
            "danger" -> holder.icon.setImageResource(R.drawable.ic_danger)
            else -> holder.icon.setImageResource(R.drawable.ic_gas_logo)
        }
    }

    fun updateData(newList: List<HistoryRecord>) {
        list = newList
        notifyDataSetChanged()
    }
}
