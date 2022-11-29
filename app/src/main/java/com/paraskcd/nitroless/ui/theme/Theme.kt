package com.paraskcd.nitroless.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = BGPrimaryDarkColor,
    primaryVariant = BGTertiaryDarkColor,
    secondary = BGSecondaryDarkColor,
    secondaryVariant = BGSecondaryDarkColor,
    background = BGPrimaryDarkColor,
    surface = BGPrimaryDarkColor,
    onPrimary = TextDarkColor,
    onSecondary = TextDarkColor
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = BGPrimaryColor,
    primaryVariant = BGTertiaryColor,
    secondary = BGSecondaryColor,
    secondaryVariant = BGSecondaryColor,
    background = BGPrimaryColor,
    surface = BGPrimaryColor,
    onPrimary = TextColor,
    onSecondary = TextColor
)

@Composable
fun NitrolessTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primaryVariant.toArgb()
            window.navigationBarColor = colors.primaryVariant.toArgb()

            if (darkTheme) {
                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightStatusBars = false

                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightNavigationBars = false
            } else {
                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightStatusBars = true

                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}