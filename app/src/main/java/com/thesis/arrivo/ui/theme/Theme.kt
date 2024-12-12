package com.thesis.arrivo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

object Theme {
    private val LightColorPalette = lightColorScheme(
        primary = LIGHT_PRIMARY,
        primaryContainer = LIGHT_PRIMARY_ALT,
        inversePrimary = LIGHT_INVERSE_PRIMARY,
        onPrimary = LIGHT_ON_PRIMARY,
        secondary = LIGHT_SECONDARY,
        secondaryContainer = LIGHT_SECONDARY_ALT,
        onSecondary = LIGHT_ON_SECONDARY,
        background = LIGHT_BACKGROUND,
        onBackground = LIGHT_ON_BACKGROUND,
        surface = LIGHT_SURFACE,
        onSurface = LIGHT_ON_SURFACE
    )

    private val DarkColorPalette = darkColorScheme(
        primary = DARK_PRIMARY,
        primaryContainer = DARK_PRIMARY_ALT,
        inversePrimary = DARK_INVERSE_PRIMARY,
        onPrimary = DARK_ON_PRIMARY,
        secondary = DARK_SECONDARY,
        secondaryContainer = DARK_SECONDARY_ALT,
        onSecondary = DARK_ON_SECONDARY,
        background = DARK_BACKGROUND,
        onBackground = DARK_ON_BACKGROUND,
        surface = DARK_SURFACE,
        onSurface = DARK_ON_SURFACE
    )

    @Composable
    fun ArrivoTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = when (darkTheme) {
            true -> DarkColorPalette
            false -> LightColorPalette
        }

        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}