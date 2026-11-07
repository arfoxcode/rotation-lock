package com.arfoxcode.rotationlock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arfoxcode.rotationlock.BuildConfig
import com.arfoxcode.rotationlock.R
import com.arfoxcode.rotationlock.data.preferences.ThemeMode
import com.arfoxcode.rotationlock.viewmodel.RotationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: RotationViewModel,
    onNavigateBack: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Appearance Section
            SettingsSectionHeader(text = stringResource(R.string.settings_appearance))

            ThemeSelector(
                current = themeMode,
                onSelect = { viewModel.setThemeMode(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingsSwitchRow(
                icon = Icons.Filled.Vibration,
                title = stringResource(R.string.settings_haptic),
                subtitle = stringResource(R.string.settings_haptic_desc),
                checked = hapticEnabled,
                onCheckedChange = { viewModel.setHapticFeedback(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Settings Section
            SettingsSectionHeader(text = stringResource(R.string.settings_quick_settings))

            SettingsInfoRow(
                icon = Icons.Filled.PhoneAndroid,
                title = stringResource(R.string.settings_quick_settings),
                subtitle = "Pull down notification shade → Edit → drag Rotation Lock tile"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // About Section
            SettingsSectionHeader(text = stringResource(R.string.settings_about))

            SettingsInfoRow(
                icon = Icons.Filled.Info,
                title = stringResource(R.string.settings_version),
                subtitle = BuildConfig.VERSION_NAME
            )

            SettingsInfoRow(
                icon = Icons.Filled.Info,
                title = stringResource(R.string.settings_about_app),
                subtitle = stringResource(R.string.settings_about_desc)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsInfoRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ThemeSelector(
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ThemeOption.entries.forEach { option ->
            val isSelected = current == option.mode
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else androidx.compose.ui.graphics.Color.Transparent
                    )
                    .clickable { onSelect(option.mode) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.label,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private enum class ThemeOption(val mode: ThemeMode, val label: String, val icon: ImageVector) {
    LIGHT(ThemeMode.LIGHT, "Light", Icons.Filled.LightMode),
    DARK(ThemeMode.DARK, "Dark", Icons.Filled.DarkMode),
    SYSTEM(ThemeMode.SYSTEM, "System", Icons.Filled.PhoneAndroid)
}
