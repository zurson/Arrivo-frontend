package com.thesis.arrivo.components.permissions

import android.content.Context
import androidx.compose.runtime.Composable
import com.thesis.arrivo.utilities.openAppSettings

@Composable
fun LocationPermissionScreen(context: Context) {
    LocationPermissionInfoScreen(
        onOpenSettingsClick = { openAppSettings(context) }
    )
}