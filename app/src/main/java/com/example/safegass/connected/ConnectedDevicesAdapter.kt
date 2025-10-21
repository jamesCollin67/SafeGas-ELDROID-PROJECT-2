package com.example.safegass.connected

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R

class ConnectedDevicesAdapter(private var devices: MutableList<Device>) :
    RecyclerView.Adapter<ConnectedDevicesAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDeviceName: TextView = view.findViewById(R.id.txtDeviceName)
        val txtDeviceInfo: TextView = view.findViewById(R.id.txtDeviceInfo)
        val txtDeviceActions: TextView = view.findViewById(R.id.txtDeviceActions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_connected_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.txtDeviceName.text = device.serial
        holder.txtDeviceInfo.text = "Battery 82%, Last seen 2 min ago"
        holder.txtDeviceActions.text = "Rename | Location | Remove"

        // Optional: handle clicks for each action
        holder.txtDeviceActions.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Options for ${device.serial}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = devices.size

    fun updateDevices(newDevices: List<Device>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
}
