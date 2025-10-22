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
        val dbRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("devices")

        holder.txtDeviceName.text = device.serial
        holder.txtDeviceInfo.text = "Location: ${device.location}"
        holder.txtDeviceActions.text = "Rename | Location | Remove"

        // 🔹 Handle clicks for each action
        holder.txtDeviceActions.setOnClickListener {
            val options = arrayOf("Rename", "Change Location", "Remove Device")

            AlertDialog.Builder(context)
                .setTitle("Manage Device: ${device.serial}")
                .setItems(options) { _, which ->
                    when (which) {
                        // ✅ Rename device
                        0 -> {
                            val input = EditText(context)
                            input.hint = "Enter new name"
                            AlertDialog.Builder(context)
                                .setTitle("Rename Device")
                                .setView(input)
                                .setPositiveButton("Save") { _, _ ->
                                    val newName = input.text.toString().trim()
                                    if (newName.isNotEmpty()) {
                                        dbRef.orderByChild("serial").equalTo(device.serial)
                                            .get().addOnSuccessListener { snapshot ->
                                                for (child in snapshot.children) {
                                                    child.ref.child("serial").setValue(newName)
                                                }
                                                device.serial = newName
                                                notifyItemChanged(holder.bindingAdapterPosition)
                                                Toast.makeText(context, "Renamed to $newName", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }

                        // ✅ Change location
                        1 -> {
                            val input = EditText(context)
                            input.hint = "Enter new location"
                            AlertDialog.Builder(context)
                                .setTitle("Change Location")
                                .setView(input)
                                .setPositiveButton("Save") { _, _ ->
                                    val newLoc = input.text.toString().trim()
                                    if (newLoc.isNotEmpty()) {
                                        dbRef.orderByChild("serial").equalTo(device.serial)
                                            .get().addOnSuccessListener { snapshot ->
                                                for (child in snapshot.children) {
                                                    child.ref.child("location").setValue(newLoc)
                                                }
                                                device.location = newLoc
                                                notifyItemChanged(holder.bindingAdapterPosition)
                                                Toast.makeText(context, "Location updated to $newLoc", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }

                        // ✅ Remove device (instant update)
                        2 -> {
                            AlertDialog.Builder(context)
                                .setTitle("Remove Device")
                                .setMessage("Are you sure you want to remove ${device.serial}?")
                                .setPositiveButton("Remove") { _, _ ->
                                    dbRef.orderByChild("serial").equalTo(device.serial)
                                        .get().addOnSuccessListener { snapshot ->
                                            for (child in snapshot.children) {
                                                child.ref.removeValue()
                                            }

                                            // ✅ Remove from local list immediately
                                            val positionToRemove = holder.bindingAdapterPosition
                                            if (positionToRemove != RecyclerView.NO_POSITION) {
                                                devices.removeAt(positionToRemove)
                                                notifyItemRemoved(positionToRemove)
                                            }

                                            Toast.makeText(context, "Device removed", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Failed to remove: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .setNegativeButton("Cancel", null)
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
