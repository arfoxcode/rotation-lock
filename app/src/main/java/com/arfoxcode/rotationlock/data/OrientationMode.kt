package com.arfoxcode.rotationlock.data

import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arfoxcode.rotationlock.R

/**
 * Represents all supported screen orientation modes.
 *
 * IMPORTANT TECHNICAL NOTE:
 * Android's Settings.System.USER_ROTATION controls the system-level rotation lock.
 * Values correspond to Surface.ROTATION_0/90/180/270.
 *
 * Settings.System.ACCELEROMETER_ROTATION = 0 means rotation is LOCKED.
 * Settings.System.ACCELEROMETER_ROTATION = 1 means AUTO ROTATE is ON.
 *
 * Not all OEM devices respect these settings equally.
 * MIUI, ColorOS, OneUI may have their own rotation layer on top.
 */
enum class OrientationMode(
    val id: String,
    @StringRes val labelRes: Int,
    @StringRes val descRes: Int,
    val iconName: String,
    /** null = auto rotate (accelerometer on), non-null = locked rotation value */
    val userRotation: Int?,
    val accelerometerRotation: Int
) {
    PORTRAIT(
        id = "portrait",
        labelRes = R.string.orientation_portrait,
        descRes = R.string.orientation_portrait_desc,
        iconName = "stay_current_portrait",
        userRotation = Settings.System.USER_ROTATION_NATURAL, // 0
        accelerometerRotation = 0
    ),
    LANDSCAPE(
        id = "landscape",
        labelRes = R.string.orientation_landscape,
        descRes = R.string.orientation_landscape_desc,
        iconName = "stay_current_landscape",
        userRotation = Settings.System.USER_ROTATION_90, // 1
        accelerometerRotation = 0
    ),
    AUTO(
        id = "auto",
        labelRes = R.string.orientation_auto,
        descRes = R.string.orientation_auto_desc,
        iconName = "screen_rotation",
        userRotation = null, // Not used when accelerometer is on
        accelerometerRotation = 1
    ),
    REVERSE_PORTRAIT(
        id = "reverse_portrait",
        labelRes = R.string.orientation_reverse_portrait,
        descRes = R.string.orientation_reverse_portrait_desc,
        iconName = "screen_rotation_alt",
        userRotation = Settings.System.USER_ROTATION_180, // 2
        accelerometerRotation = 0
    ),
    REVERSE_LANDSCAPE(
        id = "reverse_landscape",
        labelRes = R.string.orientation_reverse_landscape,
        descRes = R.string.orientation_reverse_landscape_desc,
        iconName = "rotate_90_degrees_ccw",
        userRotation = Settings.System.USER_ROTATION_270, // 3
        accelerometerRotation = 0
    ),
    SENSOR_LANDSCAPE(
        id = "sensor_landscape",
        labelRes = R.string.orientation_sensor_landscape,
        descRes = R.string.orientation_sensor_landscape_desc,
        iconName = "screen_lock_landscape",
        userRotation = Settings.System.USER_ROTATION_90, // Will also allow 270 via sensor
        accelerometerRotation = 0 // Not full auto; handled in controller
    );

    val isLocked: Boolean get() = this != AUTO
}
