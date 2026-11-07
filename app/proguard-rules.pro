# Add project specific ProGuard rules here.

# Keep application class
-keep class com.arfoxcode.rotationlock.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Jetpack Compose
-keep class androidx.compose.** { *; }

# DataStore
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}
