# Contributing to Rotation Lock

Thank you for your interest in contributing! This document explains how to get started.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Getting Started](#getting-started)
3. [Project Structure](#project-structure)
4. [Development Workflow](#development-workflow)
5. [Coding Standards](#coding-standards)
6. [Commit Messages](#commit-messages)
7. [Pull Requests](#pull-requests)
8. [Running Tests](#running-tests)

---

## Prerequisites

- **Android Studio** Ladybug (2024.2.1) or newer
- **JDK 17** (bundled with Android Studio)
- **Android SDK** with API 24–35 installed
- **Git** 2.x

---

## Getting Started

```bash
# 1. Fork the repository on GitHub

# 2. Clone your fork
git clone https://github.com/YOUR_USERNAME/rotation-lock.git
cd rotation-lock

# 3. Add the upstream remote
git remote add upstream https://github.com/arfoxcode/rotation-lock.git

# 4. Open in Android Studio
# File → Open → select the rotation-lock folder

# 5. Let Gradle sync complete (first sync downloads ~300MB)
```

Android Studio will automatically detect the project structure.

---

## Project Structure

```
app/src/main/java/com/arfoxcode/rotationlock/
├── MainActivity.kt              # Entry point
├── ui/
│   ├── AppNavigation.kt         # NavHost + routes
│   ├── screens/
│   │   ├── MainScreen.kt        # Home screen
│   │   └── SettingsScreen.kt    # Settings screen
│   ├── components/              # Reusable Composables
│   └── theme/                   # Color, Type, Theme
├── viewmodel/
│   └── RotationViewModel.kt     # UI state + business logic bridge
├── data/
│   ├── OrientationMode.kt       # Orientation mode enum
│   ├── RotationState.kt         # UI state data class
│   ├── preferences/             # DataStore
│   └── repository/              # RotationRepository
├── service/
│   └── RotationTileService.kt   # Quick Settings Tile
└── utils/
    ├── RotationController.kt    # Settings.System API
    ├── PermissionHelper.kt      # WRITE_SETTINGS permission
    └── HapticHelper.kt          # Vibration
```

**Key principle:** `RotationController` talks to `Settings.System` only. It does **not** use `Activity.requestedOrientation` — that only affects the current activity.

---

## Development Workflow

```bash
# Always branch from main
git checkout main
git pull upstream main
git checkout -b feature/your-feature-name

# Make your changes, then:
./gradlew testDebugUnitTest   # Run unit tests
./gradlew lintDebug           # Run lint
./gradlew assembleDebug       # Build debug APK

# Commit and push
git add .
git commit -m "feat: add your feature description"
git push origin feature/your-feature-name

# Open a Pull Request on GitHub
```

---

## Coding Standards

### Kotlin Style

Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and the official Android [Kotlin style guide](https://developer.android.com/kotlin/style-guide).

Key points:

- Use `val` over `var` wherever possible
- Prefer `data class` for state models
- Use `sealed class` / `enum class` for exhaustive sets
- Keep functions small and single-purpose
- Write KDoc for public functions and non-obvious logic

### Jetpack Compose

- Composables are functions — keep them **stateless** where possible
- Hoist state up to the ViewModel; pass lambdas down
- Avoid business logic inside `@Composable` functions
- Use `remember` and `derivedStateOf` appropriately
- Preview all major Composables with `@Preview`

### Architecture Rules

- **ViewModel** does not import Android UI classes (no `Context` in ViewModel — use `AndroidViewModel` only when necessary)
- **Repository** is the single source of truth for data operations
- **UI layer** observes `StateFlow` via `collectAsState()`; it does not call repository directly

### Strings

All user-visible strings go in `app/src/main/res/values/strings.xml`. Never hardcode UI strings in Kotlin or Compose files.

---

## Commit Messages

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <short description>

[optional body]

[optional footer]
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `ci`

Examples:
```
feat(tile): add subtitle showing current orientation mode
fix(controller): handle null USER_ROTATION on some OEM devices
docs(readme): add Xiaomi MIUI troubleshooting note
test(orientation): add test for SENSOR_LANDSCAPE accelerometer value
```

---

## Pull Requests

- One feature or fix per PR
- Fill in the PR template completely
- Link the related issue (`Closes #123`)
- Ensure CI passes before requesting review
- Keep the diff focused — avoid unrelated whitespace or reformatting changes

---

## Running Tests

```bash
# Unit tests only (fast, no emulator needed)
./gradlew testDebugUnitTest

# View test report
open app/build/reports/tests/testDebugUnitTest/index.html

# Lint
./gradlew lintDebug

# Full check (tests + lint)
./gradlew check
```

Instrumentation tests require a connected device or emulator:
```bash
./gradlew connectedDebugAndroidTest
```
