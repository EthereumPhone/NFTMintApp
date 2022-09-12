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


@Composable
fun NftCreatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = DarkColorPalette
    CompositionLocalProvider(LocalSpacing provides Spacing()){
        MaterialTheme(
            colors = colors,
            typography = Typography(),
            shapes = Shapes(),
            content = content
        )
    }
}