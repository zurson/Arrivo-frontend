package com.thesis.arrivo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

object Theme {
//    private val LightColorPalette = lightColorScheme(
//        primary = LIGHT_PRIMARY,
//        primaryContainer = LIGHT_PRIMARY_ALT,
//        inversePrimary = LIGHT_INVERSE_PRIMARY,
//        onPrimary = LIGHT_ON_PRIMARY,
//        secondary = LIGHT_SECONDARY,
//        secondaryContainer = LIGHT_SECONDARY_ALT,
//        onSecondary = LIGHT_ON_SECONDARY,
//        background = LIGHT_BACKGROUND,
//        onBackground = LIGHT_ON_BACKGROUND,
//        surface = LIGHT_SURFACE,
//        onSurface = LIGHT_ON_SURFACE,
//        error = ERROR,
//        surfaceContainer = LOADING_SCREEN_BG,
//
//    )
//
//    private val DarkColorPalette = darkColorScheme(
//        primary = DARK_PRIMARY,
//        primaryContainer = DARK_PRIMARY_ALT,
//        inversePrimary = DARK_INVERSE_PRIMARY,
//        onPrimary = DARK_ON_PRIMARY,
//        secondary = DARK_SECONDARY,
//        secondaryContainer = DARK_SECONDARY_ALT,
//        onSecondary = DARK_ON_SECONDARY,
//        background = DARK_BACKGROUND,
//        onBackground = DARK_ON_BACKGROUND,
//        surface = DARK_SURFACE,
//        onSurface = DARK_ON_SURFACE,
//        error = ERROR,
//        surfaceContainer = LOADING_SCREEN_BG
//    )

    private val LightColorPalette = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
    )

    private val DarkColorPalette = darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
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