package com.example.safegass.connected

import android.content.Context

class ConnectedDevicesPresenter(
    private val view: ConnectedDevicesContract.View,
    context: Context
) : ConnectedDevicesContract.Presenter {

    private val model = ConnectedDevicesModel(context)

    override fun loadDevices() {
        val devices = model.getDevices()
        view.showDevices(devices)
    }

    override fun addDevice(serial: String, location: String) {
        if (serial.isBlank() || location.isBlank()) {
            view.showMessage("Please enter both serial and location.")
            return
        }

        val newDevice = Device(serial, location)
        model.saveDevice(newDevice)
        view.showMessage("Device added successfully.")
        view.showDevices(model.getDevices())
    }

    override fun scanQRCode() {
        view.showMessage("Scanning QR Code... (feature coming soon)")
    }
}
