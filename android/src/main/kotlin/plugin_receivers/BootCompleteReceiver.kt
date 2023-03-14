package plugin_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.charger_status.PluginEvent
import com.example.charger_status.PluginEventEmitter
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterCallbackInformation
import plugin_utils.HEADLEASS_DISPATCHER_HANDLE
import plugin_utils.PluginPreferences

private const val TAG = "BootCompleteReceiver"
class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BootCompleteReceiver", "onReceive: ${intent.action}")

        when(intent.action){
            "android.intent.action.BOOT_COMPLETED" -> {
                    PluginEventEmitter.emitEvent(PluginEvent.BootCompleted)
            }
        }
    }




}