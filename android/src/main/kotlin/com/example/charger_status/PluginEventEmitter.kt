package com.example.charger_status

import android.util.Log
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.*
import java.util.concurrent.ConcurrentLinkedDeque

object PluginEventEmitter : StreamHandler {

    private var eventSink: EventChannel.EventSink? = null
    private var cachedEvents = ConcurrentLinkedDeque<PluginEvent>()
    override fun onListen(arguments: Any?, events: EventSink?) {
        Log.i("PluginEventEmitter", "onListen: ")
        events?.let {
            eventSink = it
        }
        if(cachedEvents.isNotEmpty()){
            Log.i("PluginEventEmitter", "cachedEvents not empty, emit cached events and clear cache")
            cachedEvents.forEach {
                emitEvent(it)
            }.also {
                cachedEvents.clear()
            }
        }
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    //Emit events from native to dart
    fun emitEvent(event: PluginEvent) {
        Log.i("PluginEventEmitter", "about to emit event: $event")
        eventSink?.success(event.toMap()) ?: run {
            Log.i("PluginEventEmitter", "Emitter isn't ready yet, adding event to cache")
            cachedEvents.add(event)
        }
    }
}


sealed class PluginEvent {
    object BootCompleted : PluginEvent()
    class BatteryLevelStatus(val batteryLevel: Int, val chargerStatus: String) : PluginEvent()
    fun toMap() : Map<String, Any?>{
        return when(this){
            is BootCompleted -> mapOf<String, Any?>("BootComplete" to null)
            is BatteryLevelStatus -> mapOf<String, Any?>("ChargerStatus" to mapOf<String, Any?>("BatteryLevel" to batteryLevel, "ChargerStatus" to chargerStatus))
        }
    }
}