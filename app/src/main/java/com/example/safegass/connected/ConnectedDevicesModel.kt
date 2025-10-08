package com.example.safegass.connected

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConnectedDevicesModel(context: Context) : ConnectedDevicesContract.Model {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("SafeGasDevices", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun getDevices(): MutableList<Device> {
        val json = prefs.getString("devices_list", null)
        return if (!json.isNullOrEmpty()) {
            try {
                val type = object : TypeToken<List<Device>>() {}.type
                val list: List<Device> = gson.fromJson(json, type) ?: emptyList()
                list.toMutableList()
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    }

    override fun saveDevice(device: Device) {
        val currentDevices = getDevices()
        currentDevices.add(device)
        val json = gson.toJson(currentDevices)
        prefs.edit().putString("devices_list", json).apply()
    }
}
