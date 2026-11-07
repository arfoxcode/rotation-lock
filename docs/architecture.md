# Architecture

Rotation Lock follows **MVVM (Model-View-ViewModel)** with a unidirectional data flow.

## Layer Diagram

```
┌─────────────────────────────────────────┐
│               UI Layer                  │
│  MainActivity → AppNavigation           │
│  MainScreen, SettingsScreen             │
│  Composable components                  │
└──────────────┬──────────────────────────┘
               │ observes StateFlow
               ▼
┌─────────────────────────────────────────┐
│            ViewModel Layer              │
│  RotationViewModel                      │
│  - rotationState: StateFlow<RotationState>  │
│  - themeMode: StateFlow<ThemeMode>      │
│  - hapticEnabled: StateFlow<Boolean>    │
└──────────────┬──────────────────────────┘
               │ calls
               ▼
┌─────────────────────────────────────────┐
│           Repository Layer              │
│  RotationRepository                     │
│  - bridges controller and preferences  │
└────────┬──────────────────┬────────────┘
         │                  │
         ▼                  ▼
┌──────────────┐  ┌──────────────────────┐
│  Controller  │  │      Preferences     │
│  Rotation-   │  │  AppPreferences      │
│  Controller  │  │  (DataStore)         │
│              │  │                      │
│  Settings.   │  │  ThemeMode           │
│  System API  │  │  HapticFeedback      │
└──────────────┘  └──────────────────────┘
```

## Key Design Decisions

### Why no Hilt?

The app has a single ViewModel and a small number of dependencies. Manual DI via `AndroidViewModel` and direct instantiation is simpler, produces a smaller APK, and avoids annotation processing overhead. The dependency graph is flat:

```
MainActivity
└── RotationViewModel (AndroidViewModel)
    ├── AppPreferences(context)
    ├── RotationController(context)
    └── RotationRepository(context, controller, preferences)
```

If the project grows to 3+ modules or needs testing mocks at scale, Hilt would be appropriate.

### Why not dynamic color (Material You)?

The app has a deliberate dark navy + electric blue color palette. Dynamic color (Android 12+) would override this with the user's wallpaper colors, breaking the visual identity. This is an intentional design decision — not an oversight.

### Why Settings.System and not ActivityInfo?

`Activity.requestedOrientation` only controls orientation for the **current activity**. It has no effect on other apps and resets when the activity is destroyed.

`Settings.System.ACCELEROMETER_ROTATION` and `Settings.System.USER_ROTATION` are system-level settings that persist across apps and device restarts. This is what other rotation lock apps (and Android's own rotation lock) use.

### Quick Settings Tile — no foreground service

`TileService` is lifecycle-managed by Android. It activates when the tile is visible and stops when it is not. No persistent service or `START_STICKY` is used. This keeps battery usage at zero when the tile is not visible.

### onResume permission re-check

WRITE_SETTINGS can be revoked at any time from system settings. `MainActivity.onResume()` calls `viewModel.refreshState()` to re-check permission on every return to the app. This ensures the UI always reflects the actual current state.

## State Flow

```
User taps orientation card
        │
        ▼
RotationViewModel.selectOrientation(mode)
        │
        ├── checks hasWriteSettingsPermission
        │   └── if false → updates errorMessage → Snackbar shown in UI
        │
        └── if true → RotationRepository.applyOrientation(mode)
                │
                ├── RotationController.applyOrientation(mode)
                │   └── Settings.System.putInt(...)
                │
                └── AppPreferences.setLastOrientation(mode.id)
                        │
                        ▼
                DataStore write (coroutine, Dispatchers.IO)
```

## Files Reference

| File | Responsibility |
|------|---------------|
| `MainActivity.kt` | Edge-to-edge setup, splash screen, `onResume` refresh |
| `AppNavigation.kt` | NavHost, route definitions |
| `MainScreen.kt` | Home UI: status indicator + orientation grid |
| `SettingsScreen.kt` | Theme selector, haptic toggle, app info |
| `RotationViewModel.kt` | All UI state, coroutine scope, error handling |
| `RotationRepository.kt` | Coordinates controller and preferences |
| `RotationController.kt` | `Settings.System` read/write, mode mapping |
| `PermissionHelper.kt` | `canWrite()` check, intent to settings page |
| `RotationTileService.kt` | Quick Settings Tile toggle logic |
| `AppPreferences.kt` | DataStore Preferences wrapper |
| `OrientationMode.kt` | Enum of all supported modes with rotation values |
| `RotationState.kt` | Immutable UI state data class |
