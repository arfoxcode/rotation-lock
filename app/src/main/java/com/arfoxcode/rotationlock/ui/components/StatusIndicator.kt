package com.arfoxcode.rotationlock.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arfoxcode.rotationlock.R

@Composable
fun StatusIndicator(
    isLocked: Boolean,
    activeLabel: String,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(
        targetValue = if (isLocked)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "status_icon_color"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isLocked)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "status_bg"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isLocked) 1.0f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Main icon circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(bgColor)
                .scale(iconScale)
        ) {
            Icon(
                imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.ScreenRotation,
                contentDescription = if (isLocked)
                    stringResource(R.string.status_locked)
                else
                    stringResource(R.string.status_unlocked),
                tint = iconColor,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Current mode label
        Text(
            text = activeLabel,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Active/Inactive badge
        ActiveBadge(isActive = isLocked)
    }
}

@Composable
private fun ActiveBadge(isActive: Boolean) {
    val badgeColor by animateColorAsState(
        targetValue = if (isActive)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        else
            MaterialTheme.colorScheme.surfaceVariant,
        label = "badge_color"
    )

    val dotColor by animateColorAsState(
        targetValue = if (isActive)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = "dot_color"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(badgeColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isActive) "Locked" else "Auto Rotate",
            style = MaterialTheme.typography.labelSmall,
            color = dotColor
        )
    }
}
