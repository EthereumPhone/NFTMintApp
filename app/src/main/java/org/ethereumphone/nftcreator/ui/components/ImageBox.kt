package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.ui.theme.md_theme_dark_onSurface

@Composable
fun ImageBox(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(md_theme_dark_onSurface.copy(alpha = .12f))

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement= Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    Icons.Outlined.Image,
                    contentDescription = "select image",
                    tint = md_theme_dark_onSurface,
                    modifier = Modifier
                        .size(32.dp)
                )
                Text(
                    text = "select your image",
                    fontSize = 12.sp,
                    color = md_theme_dark_onSurface
                )
            }
        }
    }
}

@Preview
@Composable
fun previewImageBox() {
    ImageBox(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(.8f)
    )
}