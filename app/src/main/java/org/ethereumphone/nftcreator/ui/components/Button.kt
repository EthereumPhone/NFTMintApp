package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.ui.theme.*

@Composable
fun ethOSButton(
    text: String,
    ButtonIcon: ImageVector = Icons.Default.ArrowUpward,
    enabled: Boolean,
    onClick: (() -> Unit)? = null
) {
    Button(
        onClick = {
            if (onClick != null) {
                onClick()
            }
        },
        shape = RoundedCornerShape(50.dp),
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()

            ,
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = gray,
            backgroundColor = white,
            contentColor = darkblue1
        )
    ) {

        Row(
            horizontalArrangement =  Arrangement.SpaceBetween,
            verticalAlignment =  Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){


            var iconcolor = darkblue1
            if(enabled) {
                iconcolor = darkblue1
            }else {
                iconcolor = Color(0xFFDCDCDC)
            }
            Box (
                modifier = Modifier

                    .clip(CircleShape)
                    .background(iconcolor)
                    .size(32.dp)
                ,
                contentAlignment= Alignment.Center
            ) {
                Icon(
                    imageVector = ButtonIcon,//Icons.Default.ArrowUpward,
                    contentDescription = "Mint",
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color.White
                )
            }
            Text(text=text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Box (
                modifier = Modifier

                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .size(28.dp)
                ,
                contentAlignment= Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Mint",
                    modifier = Modifier
                        .size(20.dp),
                    tint = Color.Transparent
                )
            }



        }

    }

}

@Composable
@Preview
fun PreviewButton() {
    NftCreatorTheme {
        //ethOSButton("Mint",Icons.Default.ArrowDownward)
    }
}