# Troubleshooting

## Rotation does not change after selecting an orientation

**Cause 1: Permission not granted**
- Check that "Modify system settings" is enabled for Rotation Lock
- Settings → Apps → Special app access → Modify system settings → Rotation Lock

**Cause 2: OEM rotation override (MIUI, HyperOS, ColorOS, OneUI)**
- Your device's vendor UI has its own rotation lock that overrides Android's system settings
- Disable the OEM rotation lock first, then try Rotation Lock again
- On MIUI/HyperOS: swipe down → tap the auto-rotate icon in the control center to OFF
- On Samsung OneUI: swipe down → tap "Portrait" or "Auto rotate" to set auto rotate first

**Cause 3: Device does not support the selected orientation**
- Not all devices support all 6 orientation modes
- Try a different mode (e.g., Portrait or Landscape)

---

## Permission banner keeps appearing

- The permission was granted but then revoked in system settings
- Reopen "Modify system settings" and re-enable it for Rotation Lock

---

## Quick Settings tile not visible

- The tile must be manually added after installation
- Pull down the notification shade → Edit → drag "Rotation Lock" tile into position
- The tile state reflects the actual system rotation setting

---

## Quick Settings tile shows "Permission needed"

- The `WRITE_SETTINGS` permission has not been granted
- Tap the tile → it will open the app → follow the permission banner

---

## Reverse Portrait / Reverse Landscape not working

- Some devices (especially tablets and older phones) do not support all rotation values
- This is a hardware/firmware limitation, not an app bug
- If the device has a hardware accelerometer lock, it may prevent non-standard rotations

---

## Sensor Landscape not behaving as expected

Sensor Landscape is a **best-effort** implementation. Android does not have a native system-level "sensor landscape only" setting.

What the app does: sets `USER_ROTATION=90` (landscape) and enables `ACCELEROMETER_ROTATION=1` (auto rotate). This allows the sensor to reach both landscape and reverse landscape.

What it cannot guarantee: the sensor will not also allow portrait rotations on some devices. For strict landscape-only locking, use "Landscape" mode instead.

---

## App crash on launch

- Ensure you are running Android 7.0 (API 24) or higher
- Try clearing the app cache: Settings → Apps → Rotation Lock → Storage → Clear Cache
- If the crash persists, [open a bug report](https://github.com/arfoxcode/rotation-lock/issues/new?template=bug_report.yml)

---

## Battery drain concern

Rotation Lock does **not** run a foreground service or background process. It writes to system settings and exits. The Quick Settings tile is lifecycle-managed by Android — it only runs when the tile is visible. Battery impact is negligible.
