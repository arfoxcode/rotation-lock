package com.arfoxcode.rotationlock.utils

import android.content.Context
import android.provider.Settings
import com.arfoxcode.rotationlock.data.OrientationMode

/**
 * Controls system-level screen rotation via Settings.System.
 *
 * HOW IT WORKS:
 * Android exposes two system settings for rotation:
 * 1. ACCELEROMETER_ROTATION: 1 = auto rotate on, 0 = locked
 * 2. USER_ROTATION: the locked rotation value (0=portrait, 1=landscape, 2=reverse portrait, 3=reverse landscape)
 *
 * This controls system-wide rotation, NOT per-app orientation.
 * Activity.requestedOrientation only affects the current activity.
 *
 * VENDOR CAVEAT:
 * Some OEMs (Xiaomi/MIUI, OPPO/ColorOS, Samsung/OneUI) layer their own
 * rotation management on top of Android's system settings. This app writes
 * to the standard Android APIs; if a vendor overrides them, the behavior
 * on those devices may differ. This is documented, not a bug in this app.
 *
 * SENSOR_LANDSCAPE special case:
 * Android does not have a native "sensor landscape" system-level setting.
 * We approximate this by enabling accelerometer rotation after setting
 * the initial rotation to landscape. This allows the sensor to switch
 * between landscape and reverse landscape only. This is a best-effort
 * implementation — full sensor landscape lock requires a custom overlay
 * service, which we deliberately avoid to keep the app lightweight.
 */
class RotationController(private val context: Context) {

    /**
     * Applies the given orientation mode to system settings.
     * Returns true if successful, false if permission is missing or write failed.
     */
    fun applyOrientation(mode: OrientationMode): Result<Unit> {
        if (!PermissionHelper.canWriteSettings(context)) {
            return Result.failure(
                SecurityException("WRITE_SETTINGS permission not granted")
            )
        }

        return try {
            when (mode) {
                OrientationMode.AUTO -> enableAutoRotate()
                OrientationMode.SENSOR_LANDSCAPE -> applySensorLandscape()
                else -> applyLockedOrientation(mode)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun enableAutoRotate() {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            1 // Auto rotate ON
        )
    }

    private fun applyLockedOrientation(mode: OrientationMode) {
        // First lock rotation (disable accelerometer)
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0 // Locked
        )
        // Then set the target rotation value
        mode.userRotation?.let { rotation ->
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.USER_ROTATION,
                rotation
            )
        }
    }

    /**
     * Best-effort sensor landscape: set landscape rotation first, then allow sensor.
     * This encourages the sensor to operate in landscape range.
     * Not all devices will honor this fully.
     */
    private fun applySensorLandscape() {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.USER_ROTATION,
            Settings.System.USER_ROTATION_90 // Start at landscape
        )
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            1 // Allow sensor, so 90 and 270 are both reachable
        )
    }

    /**
     * Reads the current system rotation state and returns the matching OrientationMode.
     * Falls back to AUTO if the current state doesn't match any known mode.
     */
    fun getCurrentMode(): OrientationMode {
        val isAutoRotate = Settings.System.getInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            1 // Default to auto if unreadable
        ) == 1

        if (isAutoRotate) return OrientationMode.AUTO

        val userRotation = Settings.System.getInt(
            context.contentResolver,
            Settings.System.USER_ROTATION,
            Settings.System.USER_ROTATION_NATURAL
        )

        return when (userRotation) {
            Settings.System.USER_ROTATION_NATURAL -> OrientationMode.PORTRAIT
            Settings.System.USER_ROTATION_90 -> OrientationMode.LANDSCAPE
            Settings.System.USER_ROTATION_180 -> OrientationMode.REVERSE_PORTRAIT
            Settings.System.USER_ROTATION_270 -> OrientationMode.REVERSE_LANDSCAPE
            else -> OrientationMode.AUTO
        }
    }
}
