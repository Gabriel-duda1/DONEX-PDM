package com.example.donex.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DonexColorScheme = lightColorScheme(
    primary = DonexBrown,
    secondary = DonexBrown,
    background = DonexCremeFundo,
    surface = DonexCremeCard,
    onPrimary = DonexWhite,
    onBackground = DonexTextDark,
    onSurface = DonexTextDark
)

@Composable
fun DonexTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window

        window.statusBarColor = DonexColorScheme.primary.toArgb()

        window.navigationBarColor = DonexColorScheme.primary.toArgb()

        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = false
    }

    MaterialTheme(
        colorScheme = DonexColorScheme,
        typography = Typography,
        content = content
    )
}