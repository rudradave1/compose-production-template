package com.rudradave.composeproductiontemplate.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.dp

private val LightColors: ColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    error = Error
)

private val DarkColors: ColorScheme = darkColorScheme(
    primary = PrimaryContainer,
    onPrimary = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = ColorTokens.DarkBackground,
    onBackground = ColorTokens.DarkOnBackground,
    surface = ColorTokens.DarkSurface,
    onSurface = ColorTokens.DarkOnSurface,
    error = Error
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

/**
 * App-wide Compose theme.
 */
@Composable
fun ComposeProductionTemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

private object ColorTokens {
    val DarkBackground = androidx.compose.ui.graphics.Color(0xFF0B1220)
    val DarkOnBackground = androidx.compose.ui.graphics.Color(0xFFE8EEF9)
    val DarkSurface = androidx.compose.ui.graphics.Color(0xFF1C2534)
    val DarkOnSurface = androidx.compose.ui.graphics.Color(0xFFDDE7F7)
}
