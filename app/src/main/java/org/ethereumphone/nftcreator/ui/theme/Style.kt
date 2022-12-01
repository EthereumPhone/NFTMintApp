package org.ethereumphone.nftcreator.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun InputFiledColors(
    textColor: Color = Color.White,
    unfocusedIndicatorColor: Color = Color.Transparent,
    focusedIndicatorColor: Color = Color.Transparent
)= TextFieldDefaults.textFieldColors(
    textColor= textColor,
    unfocusedIndicatorColor = unfocusedIndicatorColor,
    focusedIndicatorColor = focusedIndicatorColor
)