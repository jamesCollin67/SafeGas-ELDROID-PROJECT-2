package com.example.safegass.connected

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.safegass.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
        val context = holder.itemView.context
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
            .child(uid).child("devices")

        holder.txtDeviceName.text = device.serial
        holder.txtDeviceInfo.text = "Location: ${device.location} | Status: ${device.status}"

        // same AlertDialog logic for rename, move, remove
        holder.txtDeviceActions.setOnClickListener {
            val options = arrayOf("Rename", "Change Location", "Remove Device")

            AlertDialog.Builder(context)
                .setTitle("Manage Device: ${device.serial}")
                .setItems(options) { _, which ->
                    when (which) {
                        // ✅ Rename
                        0 -> {
                            val input = EditText(context)
                            input.hint = "Enter new name"
                            AlertDialog.Builder(context)
                                .setTitle("Rename Device")
                                .setView(input)
                                .setPositiveButton("Save") { _, _ ->
                                    val newName = input.text.toString().trim()
                                    if (newName.isNotEmpty()) {
                                        dbRef.child(device.serial).removeValue()
                                        dbRef.child(newName).setValue(device.copy(serial = newName))
                                        Toast.makeText(context, "Renamed to $newName", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }

                        // ✅ Change Location
                        1 -> {
                            val input = EditText(context)
                            input.hint = "Enter new location"
                            AlertDialog.Builder(context)
                                .setTitle("Change Location")
                                .setView(input)
                                .setPositiveButton("Save") { _, _ ->
                                    val newLoc = input.text.toString().trim()
                                    if (newLoc.isNotEmpty()) {
                                        dbRef.child(device.serial).child("location").setValue(newLoc)
                                        Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }

                        // ✅ Remove
                        2 -> {
                            AlertDialog.Builder(context)
                                .setTitle("Remove Device")
                                .setMessage("Remove ${device.serial}?")
                                .setPositiveButton("Yes") { _, _ ->
                                    dbRef.child(device.serial).removeValue()
                                    Toast.makeText(context, "Device removed", Toast.LENGTH_SHORT).show()
                                }
                                .setNegativeButton("No", null)
                                .show()
                        }
                    }
                }
                .show()
        }
    }

    override fun getItemCount() = devices.size

    fun updateDevices(newDevices: List<Device>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
}
