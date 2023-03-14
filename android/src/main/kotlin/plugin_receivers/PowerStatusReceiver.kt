package plugin_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.example.charger_status.PluginEvent
import com.example.charger_status.PluginEventEmitter

class PowerStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        Log.i("PowerStatusReceiver", "onReceive: ${intent.action}")
        when (intent.action){
            "android.intent.action.BATTERY_CHANGED" -> {
                val batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                batteryLevel * 100 / scale.toFloat()
                val powerStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val chargingStatus = getChargingStatus(powerStatus)
                PluginEventEmitter.emitEvent(PluginEvent.BatteryLevelStatus(batteryLevel = batteryLevel, chargingStatus))
            }
            else -> {

            }
        }
    }


    private fun getChargingStatus(powerStatus : Int) : String {
        return when(powerStatus) {
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            else -> "Unplugged"
        }

    }

    companion object {
        fun registerPowerChangesBroadcast(context: Context) {
            val filter = IntentFilter()
            val powerConnected = "android.intent.action.ACTION_POWER_CONNECTED"
            val powerDisconnected = "android.intent.action.ACTION_POWER_DISCONNECTED"
            val batteryChanged = "android.intent.action.BATTERY_CHANGED"
            filter.addAction(powerConnected)
            filter.addAction(powerDisconnected)
            filter.addAction(batteryChanged)
            context.registerReceiver(PowerStatusReceiver(), filter)
            Log.i("PowerStatusReceiver", "registerPowerChangesBroadcast: ")
        }

        fun unRegisterPowerChangesBroadcast(context: Context, receiver: BroadcastReceiver) {
            context.unregisterReceiver(receiver)
        }
    }
}