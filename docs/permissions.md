# Permissions

Rotation Lock uses exactly **one** permission: `WRITE_SETTINGS`.

## Why This Permission Is Needed

Android controls screen rotation through two system settings:

| Setting | Values |
|---------|--------|
| `Settings.System.ACCELEROMETER_ROTATION` | `1` = auto rotate on, `0` = locked |
| `Settings.System.USER_ROTATION` | `0` = portrait, `1` = landscape, `2` = reverse portrait, `3` = reverse landscape |

Writing to these settings requires the `WRITE_SETTINGS` permission — specifically the "Modify system settings" special access.

## Why It Cannot Be Auto-Granted

This is a **special app access permission**, not a regular runtime permission. It cannot be granted via `ActivityResultContracts.RequestPermission()`. The user must enable it manually:

**Settings → Apps → Special app access → Modify system settings → Rotation Lock → Allow**

This is an Android security design decision. Apps that modify system settings must have explicit user consent through the system settings UI.

## What This Permission Does NOT Allow

- Read contacts, messages, or location
- Access camera or microphone
- Read files or storage
- Make network requests
- Access any personal data

The `WRITE_SETTINGS` permission only allows writing to a small set of Android system settings. Rotation Lock only writes to `ACCELEROMETER_ROTATION` and `USER_ROTATION`.

## Permission Flow in the App

1. App launches → checks `Settings.System.canWrite(context)`
2. If **not granted** → shows `PermissionBanner` with explanation + "Open Settings" button
3. User taps "Open Settings" → navigated to `Settings.ACTION_MANAGE_WRITE_SETTINGS` for this app's package
4. User grants permission in system settings and returns to app
5. `MainActivity.onResume()` → calls `viewModel.refreshState()` → re-checks permission
6. Banner disappears and orientation controls become active

## Vendor-Specific Behavior

Some OEM Android skins (MIUI, HyperOS, ColorOS, OneUI) layer their own rotation management on top of Android's system settings. In these cases:

- The permission may still be required
- The system settings write may succeed but the OEM layer may override it
- The device's own rotation lock (in the status bar or settings) may need to be disabled first

This is not a bug in Rotation Lock — it is a limitation of the OEM firmware.
