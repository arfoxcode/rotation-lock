package com.arfoxcode.rotationlock.service

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.arfoxcode.rotationlock.R
import com.arfoxcode.rotationlock.utils.PermissionHelper
import com.arfoxcode.rotationlock.utils.RotationController

/**
 * Quick Settings Tile for Rotation Lock.
 *
 * Behavior:
 * - Tap: Toggle between AUTO ROTATE and PORTRAIT LOCK.
 * - Tile state reflects the actual system setting.
 * - Does NOT use a foreground service. TileService is lifecycle-managed by Android.
 *
 * The tile reads/writes directly via RotationController because
 * TileService runs in a separate process context from the main app.
 */
class RotationTileService : TileService() {

    private lateinit var rotationController: RotationController

    override fun onCreate() {
        super.onCreate()
        rotationController = RotationController(this)
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (!PermissionHelper.canWriteSettings(this)) {
            // Can't toggle without permission; open the app to guide the user
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // On API 24+ we can collapse the panel and open the app
                try {
                    startActivityAndCollapse(
                        android.content.Intent(this, Class.forName("com.arfoxcode.rotationlock.MainActivity"))
                            .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (e: ClassNotFoundException) {
                    // No-op
                }
            }
            updateTile()
            return
        }

        // Toggle: if currently locked → auto, if auto → portrait lock
        val isAutoRotate = Settings.System.getInt(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            1
        ) == 1

        if (isAutoRotate) {
            // Lock to portrait
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0)
            Settings.System.putInt(contentResolver, Settings.System.USER_ROTATION, 0)
        } else {
            // Enable auto rotate
            Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 1)
        }

        updateTile()
    }

    private fun updateTile() {
        val tile = qsTile ?: return

        val hasPermission = PermissionHelper.canWriteSettings(this)
        val isAutoRotate = if (hasPermission) {
            Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                1
            ) == 1
        } else {
            true // Assume auto if no permission to read
        }

        tile.state = when {
            !hasPermission -> Tile.STATE_UNAVAILABLE
            isAutoRotate -> Tile.STATE_INACTIVE  // Auto rotate = not locked = inactive
            else -> Tile.STATE_ACTIVE             // Locked = active
        }

        tile.label = getString(R.string.tile_label)
        tile.contentDescription = if (isAutoRotate) {
            getString(R.string.status_unlocked)
        } else {
            getString(R.string.status_locked)
        }

        // Update subtitle on API 29+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = if (!hasPermission) {
                "Permission needed"
            } else if (isAutoRotate) {
                getString(R.string.orientation_auto)
            } else {
                val rotation = Settings.System.getInt(
                    contentResolver,
                    Settings.System.USER_ROTATION,
                    0
                )
                when (rotation) {
                    0 -> getString(R.string.orientation_portrait)
                    1 -> getString(R.string.orientation_landscape)
                    2 -> getString(R.string.orientation_reverse_portrait)
                    3 -> getString(R.string.orientation_reverse_landscape)
                    else -> getString(R.string.status_locked)
                }
            }
        }

        tile.updateTile()
    }
}
