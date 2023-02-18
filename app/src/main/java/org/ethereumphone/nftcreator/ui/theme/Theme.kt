package org.ethereumphone.nftcreator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorPalette = darkColors(
    primary = Purple500,
    primaryVariant = Purple200,
    secondary = Purple700,
    background = black900
)

private val darkColors = darkColors(
    primary = darkblue1,
    onPrimary = white,
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
    CompositionLocalProvider(LocalSpacing provides Spacing()){
        MaterialTheme(
            colors = colors,
            content = content,
            shapes = shapes
        )
    }
}