package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFFCCC2DC),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF2B2930),
    onSurface = Color(0xFFE6E1E5),
    outline = Color(0xFF49454F)
  )

private val LightColorScheme = DarkColorScheme // Keep consistent theme for utility panel

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to dark theme for this tactical panel
  dynamicColor: Boolean = false, // Disable dynamic developer accents to prioritize theme palette
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme // Always use our customized dark tactical theme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
