# Installation

## Option 1: Download APK (Recommended for End Users)

1. Go to [Releases](https://github.com/arfoxcode/rotation-lock/releases)
2. Download the latest `app-release.apk`
3. Verify the SHA-256 checksum (see below)
4. Enable "Install from unknown sources" in your device settings if prompted
5. Install the APK

### Verify APK Checksum

Each release includes a `.sha256` file. To verify:

```bash
# Linux / macOS
sha256sum -c app-release.apk.sha256

# Windows (PowerShell)
Get-FileHash app-release.apk -Algorithm SHA256
# Compare the output with the contents of app-release.apk.sha256
```

## Option 2: Build from Source

### Requirements

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17
- Android SDK with API 24–35

### Steps

```bash
git clone https://github.com/arfoxcode/rotation-lock.git
cd rotation-lock
./gradlew assembleRelease
```

The APK will be at `app/build/outputs/apk/release/app-release-unsigned.apk`.

## First Launch

After installation:

1. Open **Rotation Lock**
2. If the permission banner appears, tap **Open Settings**
3. Enable **"Modify system settings"** for Rotation Lock
4. Return to the app — the permission banner disappears
5. Tap any orientation card to lock your screen

## Adding the Quick Settings Tile

1. Pull down your notification shade twice (to reach Quick Settings)
2. Tap the **Edit / Pencil** icon
3. Find **Rotation Lock** in the available tiles
4. Drag it to your active tiles area
5. Done — the tile toggles between Auto Rotate and Portrait Lock
