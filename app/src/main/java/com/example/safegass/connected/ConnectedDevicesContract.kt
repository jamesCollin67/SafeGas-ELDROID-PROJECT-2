package com.example.safegass.connected

interface ConnectedDevicesContract {
    interface View {
        fun showDevices(devices: List<Device>)
        fun showMessage(message: String)
        fun clearInputFields()
    }

    interface Presenter {
        fun loadDevices()
        fun addDevice(serial: String, location: String)
        fun scanQRCode()
    }

    interface Model {
        fun getDevices(): MutableList<Device>
        fun saveDevice(device: Device)
    }
}
