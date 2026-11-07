package com.arfoxcode.rotationlock.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.ScreenLockLandscape
import androidx.compose.material.icons.filled.ScreenLockPortrait
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.StayCurrentLandscape
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.ui.graphics.vector.ImageVector
import com.arfoxcode.rotationlock.data.OrientationMode

/**
 * Maps OrientationMode to appropriate Material Design icons.
 *
 * We use material-icons-extended for the full icon set.
 * Icons are chosen to intuitively represent each orientation.
 */
object OrientationIconMapper {
    fun getIcon(mode: OrientationMode): ImageVector = when (mode) {
        OrientationMode.PORTRAIT -> Icons.Filled.StayCurrentPortrait
        OrientationMode.LANDSCAPE -> Icons.Filled.StayCurrentLandscape
        OrientationMode.AUTO -> Icons.Filled.ScreenRotation
        OrientationMode.REVERSE_PORTRAIT -> Icons.Filled.ScreenLockPortrait
        OrientationMode.REVERSE_LANDSCAPE -> Icons.Filled.ScreenLockLandscape
        OrientationMode.SENSOR_LANDSCAPE -> Icons.Filled.Autorenew
    }
}
