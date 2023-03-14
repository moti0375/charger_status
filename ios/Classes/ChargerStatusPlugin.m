#import "ChargerStatusPlugin.h"
#if __has_include(<charger_status/charger_status-Swift.h>)
#import <charger_status/charger_status-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "charger_status-Swift.h"
#endif

@implementation ChargerStatusPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftChargerStatusPlugin registerWithRegistrar:registrar];
}
@end
