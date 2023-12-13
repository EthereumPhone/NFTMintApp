package org.ethereumphone.nftcreator.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColors(
    primary = Purple500,
    primaryVariant = Purple200,
    secondary = Purple700,
    background = black900
)

@SuppressLint("ConflictingOnColor")
private val darkColors = darkColors(
    primary = darkblue1,
    onPrimary = Color.White,
    secondary = darkblue2,
    onSecondary = white,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = darkblue1,
    surface = darkblue2,
    onSurface = white,
)



@Composable
fun NftCreatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    shapes: Shapes = Shapes(),
    content: @Composable () -> Unit
) {
    val colors = darkColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb() // change color status bar here
            window.navigationBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()){
        MaterialTheme(
            colors = colors,
            typography = ethOSTypography,
            content = content,
            shapes = shapes
        )
    }
}