package com.example.composegitapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightThemeColors = lightColorScheme(

)

private val DarkThemeColors = darkColorScheme(

)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
