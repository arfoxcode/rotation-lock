<div align="center">

<img src="docs/screenshots/icon_placeholder.png" width="96" alt="Rotation Lock Icon" />

# Rotation Lock

**Take full control of your Android screen orientation.**  
A lightweight, open-source Android app to lock, unlock, and manage screen rotation — with a modern Material 3 UI and zero background services.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-orange.svg)](https://developer.android.com/tools/releases/platforms)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4.svg)](https://developer.android.com/compose)
[![Build Status](https://img.shields.io/github/actions/workflow/status/arfoxcode/rotation-lock/android-ci.yml?branch=main&label=CI)](https://github.com/arfoxcode/rotation-lock/actions/workflows/android-ci.yml)
[![Latest Release](https://img.shields.io/github/v/release/arfoxcode/rotation-lock)](https://github.com/arfoxcode/rotation-lock/releases/latest)
[![Downloads](https://img.shields.io/github/downloads/arfoxcode/rotation-lock/total)](https://github.com/arfoxcode/rotation-lock/releases)

[Download APK](#download) · [Build from Source](#build-from-source) · [Report Bug](https://github.com/arfoxcode/rotation-lock/issues/new?template=bug_report.yml) · [Request Feature](https://github.com/arfoxcode/rotation-lock/issues/new?template=feature_request.yml)

</div>

---

> **Rotation Lock** is a free, open-source Android rotation lock app built with Kotlin and Jetpack Compose. It gives you precise control over screen orientation — portrait, landscape, reverse portrait, reverse landscape, auto rotate, and sensor landscape — using Android's native system settings API. No ads, no analytics, no account required.

---

## Screenshots

> _Screenshots will be added with the first public release._
>
> | Main Screen (Dark) | Main Screen (Light) | Settings | Quick Settings Tile |
> |---|---|---|---|
> | ![Main Dark](docs/screenshots/main_dark.png) | ![Main Light](docs/screenshots/main_light.png) | ![Settings](docs/screenshots/settings.png) | ![Tile](docs/screenshots/tile.png) |

---

## Key Features

- 🔒 **Lock screen rotation** to any of 6 orientation modes
- 🔄 **Auto rotate** via device sensor
- ⚡ **Quick Settings Tile** — toggle rotation lock without opening the app
- 🎨 **Material 3 design** with dark, light, and system-follow themes
- 📳 **Haptic feedback** on orientation change (optional)
- 🔋 **Zero battery impact** — no background service or persistent process
- 🔐 **Privacy first** — no data collection, no internet permission, no account
- 📱 **Android 7.0 to 15** support (API 24–35)

---

## Supported Orientation Modes

| Mode | Description |
|------|-------------|
| Portrait | Lock to standard upright orientation |
| Landscape | Lock to 90° landscape (left) |
| Auto Rotate | Follow the device's accelerometer sensor |
| Reverse Portrait | Lock to 180° upside-down portrait |
| Reverse Landscape | Lock to 270° landscape (right) |
| Sensor Landscape | Allow sensor to pick between landscape and reverse landscape |

---

## Download

<div align="center">

**[⬇ Download Latest APK](https://github.com/arfoxcode/rotation-lock/releases/latest)**

Always verify the SHA-256 checksum included with each release before installing.

</div>

---

## Build from Source

### Requirements

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK with API 24–35

### Steps

```bash
git clone https://github.com/arfoxcode/rotation-lock.git
cd rotation-lock
./gradlew assembleRelease
# APK: app/build/outputs/apk/release/
```

See [docs/development.md](docs/development.md) for the full development guide.

---

## Required Permissions

| Permission | Why |
|-----------|-----|
| `WRITE_SETTINGS` | Required to write `Settings.System.ACCELEROMETER_ROTATION` and `Settings.System.USER_ROTATION` — the two system settings that control rotation lock on Android. |

This is a **special app access permission**. It cannot be auto-granted — the user must enable it manually:

> Settings → Apps → Special app access → Modify system settings → Rotation Lock → Allow

See [docs/permissions.md](docs/permissions.md) for the full explanation.

---

## Compatibility

| Android Version | API Level | Status |
|----------------|-----------|--------|
| Android 15 | 35 | ✅ Supported |
| Android 14 | 34 | ✅ Supported |
| Android 13 | 33 | ✅ Supported |
| Android 12 / 12L | 31–32 | ✅ Supported |
| Android 11 | 30 | ✅ Supported |
| Android 10 | 29 | ✅ Supported |
| Android 8 / 9 | 26–28 | ✅ Supported |
| Android 7 / 7.1 | 24–25 | ✅ Supported (minimum) |

### Vendor Notes

Some OEM Android skins (MIUI/HyperOS, ColorOS, OneUI) layer their own rotation management on top of Android's system settings. The app writes to standard Android APIs; if a vendor overrides them, results may differ on those devices. See [docs/troubleshooting.md](docs/troubleshooting.md).

---

## Architecture Overview

The app uses MVVM with unidirectional data flow:

```
UI (Jetpack Compose)
  └── RotationViewModel (StateFlow)
        └── RotationRepository
              ├── RotationController  → Settings.System API
              └── AppPreferences      → DataStore Preferences
```

No dependency injection framework — Manual DI is sufficient for this scope.  
See [docs/architecture.md](docs/architecture.md) for a full breakdown.

---

## GitHub Actions & Automated Releases

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| `android-ci.yml` | Push / PR to `main` | Build, lint, unit tests, upload debug APK |
| `release.yml` | Push `v*` tag | Build release APK, generate SHA-256, create GitHub Release |
| `dependency-check.yml` | Weekly (Monday) | Check for outdated dependencies |

To create a release:
```bash
git tag v1.0.0
git push origin v1.0.0
# GitHub Actions handles the rest
```

---

## Roadmap

- [x] Six orientation modes
- [x] Quick Settings Tile
- [x] Dark / Light / System theme
- [x] Haptic feedback
- [x] GitHub Actions CI/CD
- [ ] Per-app rotation profiles
- [ ] Home screen / notification widget
- [ ] Rotation schedule (time-based auto-lock)

---

## Contributing

Contributions are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) first.

```bash
# Fork → clone → branch
git checkout -b feature/your-feature
# Make changes, run tests
./gradlew testDebugUnitTest lintDebug
# Open a Pull Request
```

---

## Privacy

Rotation Lock does **not**:

- Collect personal data
- Require internet access
- Use analytics or crash reporting
- Require an account or login
- Send data to any server

The only data stored is your theme preference and haptic setting — locally on your device via Android DataStore.

---

## License

```
MIT License — Copyright (c) 2024 arfoxcode
```

See [LICENSE](LICENSE) for the full text.

---

## Author

**arfoxcode**  
GitHub: [@arfoxcode](https://github.com/arfoxcode)  
Repository: [github.com/arfoxcode/rotation-lock](https://github.com/arfoxcode/rotation-lock)

---

<div align="center">

If this project helped you, consider giving it a ⭐ on GitHub.

</div>
