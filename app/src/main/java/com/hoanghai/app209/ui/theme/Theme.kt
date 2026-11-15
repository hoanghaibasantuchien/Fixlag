package com.hoanghai.app209.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Primary, onPrimary = OnPrimary,
    secondary = Secondary, background = Background, surface = Surface,
    onBackground = OnBackground
)

private val LightColors = lightColorScheme(
    primary = PrimaryDark, onPrimary = OnPrimary, secondary = Secondary
)

@Composable
fun HoangHaiTheme(useDark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (useDark) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
