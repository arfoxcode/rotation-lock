# Development Guide

## Environment Setup

### Required

| Tool | Version |
|------|---------|
| Android Studio | Ladybug (2024.2.1)+ |
| JDK | 17 (bundled with AS) |
| Kotlin | 2.1.0 |
| AGP | 8.7.3 |
| compileSdk | 35 |
| minSdk | 24 |

### Gradle Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (unsigned)
./gradlew assembleRelease

# Run unit tests
./gradlew testDebugUnitTest

# Run lint
./gradlew lintDebug

# Full check (tests + lint)
./gradlew check

# Clean build
./gradlew clean assembleDebug

# View dependency tree
./gradlew app:dependencies
```

## Testing on a Real Device

Rotation lock requires `WRITE_SETTINGS` — this cannot be tested on the emulator via the normal permission flow because the emulator may not have a hardware rotation sensor.

For the best testing experience:
1. Use a physical Android device
2. Enable developer options
3. Install the debug APK via ADB: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
4. Test the full permission grant flow

## Release Process

```bash
# 1. Update version in app/build.gradle.kts
#    versionCode = X
#    versionName = "X.Y.Z"

# 2. Update CHANGELOG.md
#    Add entry under [X.Y.Z] - YYYY-MM-DD

# 3. Commit
git add .
git commit -m "chore: bump version to X.Y.Z"
git push origin main

# 4. Tag the release
git tag vX.Y.Z
git push origin vX.Y.Z

# GitHub Actions release.yml triggers automatically
# A GitHub Release is created with the APK and SHA-256 checksum
```

## Signed Release Setup (GitHub Actions)

To produce a signed APK in CI, add these secrets to your GitHub repository
(Settings → Secrets and variables → Actions):

| Secret | Description |
|--------|-------------|
| `KEYSTORE_BASE64` | Base64-encoded `.jks` keystore file |
| `KEY_ALIAS` | Key alias in the keystore |
| `KEY_PASSWORD` | Key password |
| `STORE_PASSWORD` | Keystore password |

Generate the base64 string:
```bash
base64 -i release.jks | tr -d '\n'
```

If these secrets are not set, the workflow builds an unsigned APK and notes this in the release description.

**Never commit the `.jks` file to the repository.** It is listed in `.gitignore`.

## Adding a New Orientation Mode

1. Add a new entry to `OrientationMode.kt` with the correct `userRotation` and `accelerometerRotation` values
2. Add string resources in `strings.xml`
3. Add an icon mapping in `OrientationIconMapper.kt`
4. Write a unit test in `OrientationModeTest.kt`
5. Test on a physical device — not all devices support all rotation values

## Code Style Enforcement

The project uses `.editorconfig` for consistent formatting. Android Studio respects this automatically.

Run `./gradlew lintDebug` before every PR — lint failures block the CI build.
