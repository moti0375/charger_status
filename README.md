# charger_status

## Flutter Background Tasks Showcase
This project is a Flutter plugin which intent to showcase flutter background tasks using a callbackDispatcher as described in [this 
article](https://medium.com/p/26815e1c4046)

The plugin will run in background and provide BootComplete and Battery and charger status events to 
the callbackDispatcher function in the example app main.dart. 

In addition, the callbackDispatcher will register to location changes once it called from native.


Currently it works only on Android. 



