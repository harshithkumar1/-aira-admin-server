package com.aira.vision.inspection.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AiraOrange,
    onPrimary = AiraCardBg,
    primaryContainer = AiraOrangeLight,
    secondary = AiraBlue,
    onSecondary = AiraCardBg,
    secondaryContainer = AiraBlue.copy(alpha = 0.1f),
    tertiary = AiraPurple,
    background = AiraBg,
    onBackground = AiraTextPrimary,
    surface = AiraCardBg,
    onSurface = AiraTextPrimary,
    surfaceVariant = AiraSurface,
    onSurfaceVariant = AiraTextSecondary,
    outline = AiraBorder,
    error = AiraRed,
    onError = AiraCardBg
)

@Composable
fun AiraVisionTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AiraNavy.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
