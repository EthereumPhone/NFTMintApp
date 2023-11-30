package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.darkblue2
import org.ethereumphone.nftcreator.ui.theme.md_theme_dark_onSurface
import org.ethereumphone.nftcreator.ui.theme.md_theme_light_onError
import org.ethereumphone.nftcreator.ui.theme.white

@Composable
fun ImageBox(
    modifier: Modifier
) {
    val Inter = FontFamily(
        Font(R.font.inter_light,FontWeight.Light),
        Font(R.font.inter_regular,FontWeight.Normal),
        Font(R.font.inter_medium,FontWeight.Medium),
        Font(R.font.inter_semibold,FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold)
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .dashedBorder(
                4.dp,
                Color(0xFF9FA2A5),
                shape=RoundedCornerShape(24.dp),
                on = 20.dp,
                off = 10.dp
            )


    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Icon(
                    Icons.Outlined.Image,
                    contentDescription = "Select image",
                    tint = Color(0xFF9FA2A5),
                    modifier = Modifier
                        .size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select image",
                    fontSize = 18.sp,
                    color = Color(0xFF9FA2A5),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Inter
                )
            }
        }
    }
}

@Preview
@Composable
fun previewImageBox() {
    ImageBox(modifier = Modifier
        .width(300.dp)
        .height(250.dp)
    )
}