package com.nammakathey.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KidFriendlyLightColors = lightColorScheme(
    primary = Color(0xFFFF9800), // Bright Orange
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFFE65100),
    secondary = Color(0xFF4CAF50), // Green
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8E6C9),
    onSecondaryContainer = Color(0xFF1B5E20),
    tertiary = Color(0xFF2196F3), // Blue
    onTertiary = Color.White,
    background = Color(0xFFFFFDE7), // Very Light Yellow
    surface = Color.White,
    onBackground = Color(0xFF3E2723),
    onSurface = Color(0xFF3E2723)
)

private val KidFriendlyDarkColors = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color(0xFF4E342E),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF1B5E20),
    tertiary = Color(0xFF64B5F6),
    background = Color(0xFF3E2723),
    surface = Color(0xFF4E342E)
)

@Composable
fun NammaKatheyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) KidFriendlyDarkColors else KidFriendlyLightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
