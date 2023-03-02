package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.darkblue2
import org.ethereumphone.nftcreator.ui.theme.md_theme_dark_onSurface
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
            .background(darkblue2)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Icon(
                    Icons.Outlined.Image,
                    contentDescription = "select image",
                    tint = white,
                    modifier = Modifier
                        .size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select",
                    fontSize = 18.sp,
                    color = white,
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
        .fillMaxWidth()
        .fillMaxHeight(.8f)
    )
}