package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BlazeOrange,
    onPrimary = Color.White,
    primaryContainer = BlazeOrangeLight,
    onPrimaryContainer = Color.White,
    secondary = GoldChampionship,
    onSecondary = CourtNavyDark,
    secondaryContainer = CourtNavyCardHover,
    onSecondaryContainer = Color.White,
    tertiary = ElectricCyan,
    onTertiary = CourtNavyDark,
    background = CourtNavyDark,
    onBackground = TextPrimary,
    surface = CourtNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = CourtNavyCard,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
    error = CrimsonRed,
    onError = Color.White
)

private val LightColorScheme = darkColorScheme(
    primary = BlazeOrange,
    onPrimary = Color.White,
    secondary = GoldChampionship,
    background = CourtNavyDark,
    surface = CourtNavySurface
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // We lock default to crisp stadium night mode for high-contrast sports feel
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
