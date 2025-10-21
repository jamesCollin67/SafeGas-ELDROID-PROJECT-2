import com.example.safegass.connected.Device

interface ConnectedDevicesContract {
    interface View {
        fun showDevices(devices: List<Device>)
        fun showMessage(message: String)
        fun clearInputFields() // 🔹 add this
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
