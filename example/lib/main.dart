import 'dart:io';
import 'dart:isolate';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:charger_status/charger_status.dart';
import 'package:geolocator/geolocator.dart';

@pragma('vm:entry-point')
void appHeadlessDispatcher() async {
  print("appHeadlessDispatcher called");
  WidgetsFlutterBinding.ensureInitialized();
  DartPluginRegistrant.ensureInitialized();

  ChargerStatus.instance.listenToEvents().listen((event) {
    print("PowerStatusEvent: ${event}");
  });

  ChargerStatus.instance.startPowerChangesListener();
  await _listenToGeoLocations();
}

Future<bool> _listenToGeoLocations() async {
  var serviceEnabled = await Geolocator.isLocationServiceEnabled();
  if (serviceEnabled) {
    print("_listenToGeoLocations: serviceEnabled: $serviceEnabled");
     late LocationSettings locationSettings;

    if (Platform.isAndroid) {
      locationSettings = AndroidSettings(
          accuracy: LocationAccuracy.high,
          distanceFilter: 0,
          forceLocationManager: true,
          //(Optional) Set foreground notification config to keep the app alive
          //when going to the background
          foregroundNotificationConfig: const ForegroundNotificationConfig(
            notificationText:
            "Getting you location in background, don't worry",
            notificationTitle: "Running in Background",
            enableWakeLock: false,
          )
      );
      Geolocator.getPositionStream(locationSettings: locationSettings).listen(
              (Position? position) {
            print(position == null ? 'Unknown' : 'onLocationChanged: ${position.latitude.toString()}, ${position.longitude.toString()}');
          });
    }
    return true;
  } else {
    return false;
  }
}


void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await _listenToGeoLocations();
  //_listenToBackgroundLocation();
  ChargerStatus.instance.registerHeadlessDispatcher(appHeadlessDispatcher);
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _batteryLevel = 'Unknown';
  String _chargerStatus = "Unknown";
  final _chargerStatusPlugin = ChargerStatus.instance;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String batteryLevel;
    String chargerStatus;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      batteryLevel = await _chargerStatusPlugin.getBatteryLevel() ?? 'Unknown platform version';
      chargerStatus = await _chargerStatusPlugin.getChargerStatus() ?? "Unable to get charger status";
    } on PlatformException {
      batteryLevel = 'Failed to get platform version.';
      chargerStatus = "Failed to get charger status";
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _batteryLevel = batteryLevel;
      _chargerStatus = chargerStatus;
    });
  }

  @override
  Widget build(BuildContext context) {
    debugPaintSizeEnabled = true;

    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [Text('Running on: $_batteryLevel\n'), Text(_chargerStatus)],
          ),
        ),
      ),
    );
  }
}
