import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ChargerStatus {
  static ChargerStatus? _instance;
  ChargerStatus._();

  static ChargerStatus get instance => _instance ??= ChargerStatus._();

  final _methodChannel = const MethodChannel('com.example.charger_status:method_channel');
  final _eventChannel = const EventChannel("com.example.charger_status/event_channel");

  Future<String?> getBatteryLevel() async =>  _methodChannel.invokeMethod<String>('getBatteryLevel');
  Future<String?> getChargerStatus() async =>  _methodChannel.invokeMethod<String>('getChargerStatus');

  Future registerHeadlessDispatcher(Function headlessDispatcher) async {

    CallbackHandle? callbackHandle = PluginUtilities.getCallbackHandle(headlessDispatcher);
    if(callbackHandle != null){
      final int dispatcherHandle = callbackHandle.toRawHandle();
      print("ChargerStatus: dispatcherHandle: $dispatcherHandle");
      await _methodChannel.invokeMethod<void>("registerHeadlessDispatcher", {"dispatcherHandler" : dispatcherHandle});
    }
  }

  Future startPowerChangesListener() async => _methodChannel.invokeMethod<void>("startPowerChangesListener");

  Stream<dynamic> listenToEvents() {
    WidgetsFlutterBinding.ensureInitialized();
    DartPluginRegistrant.ensureInitialized();
    return _eventChannel.receiveBroadcastStream();
  }
}
