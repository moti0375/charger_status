package initializer

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.startup.Initializer
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterCallbackInformation
import plugin_receivers.PowerStatusReceiver
import plugin_utils.HEADLEASS_DISPATCHER_HANDLE
import plugin_utils.PluginPreferences

private const val TAG = "AppSta"
class AppStartup : Initializer<Unit> {
    private lateinit var flutterEngine: FlutterEngine

    override fun create(context: Context) {
        Log.i("AppStartup", "create")
        PowerStatusReceiver.registerPowerChangesBroadcast(context)
        initializeFlutterEngine(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    private fun initializeFlutterEngine(context: Context) {
        Log.i("BootCompleteReceiver", "initializeFlutterEngine...")
        val flutterLoader = FlutterLoader()

        if(!flutterLoader.initialized()){
            flutterLoader.let {
                it.startInitialization(context.applicationContext)
                it.ensureInitializationCompleteAsync(context.applicationContext, null, Handler(Looper.getMainLooper())){
                    Log.i(TAG, "ensureInitializationComplete, completed..")
                    val bundlePath = it.findAppBundlePath()
                    // setCampaignTriggerHandler(context, bundlePath)
                    executeDartCallback(context, bundlePath) //Call single shot when created..
                }
            }
        }
    }

    private fun executeDartCallback(context: Context, bundlePath: String) {
        flutterEngine = FlutterEngine(context.applicationContext)
        val dispatcherHandler : Long = PluginPreferences.getLongValue(context, HEADLEASS_DISPATCHER_HANDLE)

        if(dispatcherHandler != -1L){
            val callbackinfo: FlutterCallbackInformation = FlutterCallbackInformation.lookupCallbackInformation(dispatcherHandler)
            Log.i(TAG, "executeDartCallback: bundlePath $bundlePath, callbackInfo: ${callbackinfo.callbackName}")
            flutterEngine.dartExecutor.
            executeDartCallback(
                DartExecutor.DartCallback(
                    context.assets,
                    bundlePath,
                    callbackinfo))
        }
    }

}