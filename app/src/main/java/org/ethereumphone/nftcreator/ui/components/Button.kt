package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.*

//@Composable
//fun ethOSButton(
//    text: String,
//    ButtonIcon: ImageVector = Icons.Default.ArrowUpward,
//    enabled: Boolean,
//    onClick: (() -> Unit)? = null
//) {
//
//    val Inter = FontFamily(
//        Font(R.font.inter_light,FontWeight.Light),
//        Font(R.font.inter_regular,FontWeight.Normal),
//        Font(R.font.inter_medium,FontWeight.Medium),
//        Font(R.font.inter_semibold,FontWeight.SemiBold),
//        Font(R.font.inter_bold, FontWeight.Bold)
//    )
//
//    Button(
//        onClick = {
//            if (onClick != null) {
//                onClick()
//            }
//        },
//        shape = RoundedCornerShape(50.dp),
//        enabled = enabled,
//        modifier = Modifier
//            .fillMaxWidth()
//
//            ,
//        colors = ButtonDefaults.buttonColors(
//            disabledBackgroundColor = gray,
//            backgroundColor = white,
//            contentColor = darkblue1
//        )
//    ) {
//
//        Row(
//            horizontalArrangement =  Arrangement.SpaceBetween,
//            verticalAlignment =  Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ){
//
//
//            var iconcolor = darkblue1
//            if(enabled) {
//                iconcolor = darkblue1
//            }else {
//                iconcolor = Color(0xFFDCDCDC)
//            }
//            Box (
//                modifier = Modifier
//
//                    .clip(CircleShape)
//                    .background(iconcolor)
//                    .size(32.dp)
//                ,
//                contentAlignment= Alignment.Center
//            ) {
//                Icon(
//                    imageVector = ButtonIcon,//Icons.Default.ArrowUpward,
//                    contentDescription = "Mint",
//                    modifier = Modifier
//                        .size(24.dp),
//                    tint = Color.White
//                )
//            }
//            Text(text=text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = Inter)
//            Box (
//                modifier = Modifier
//
//                    .clip(CircleShape)
//                    .background(Color.Transparent)
//                    .size(28.dp)
//                ,
//                contentAlignment= Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowUpward,
//                    contentDescription = "Mint",
//                    modifier = Modifier
//                        .size(20.dp),
//                    tint = Color.Transparent
//                )
//            }
//
//
//
//        }
//
//    }
//
//}

@Composable
fun ethOSButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource =
        remember { MutableInteractionSource() }
) {

    //val interactionSource = remember { MutableInteractionSource() }
    //val isPressed by interactionSource.collectIsPressedAsState() //if pressed
    //val isHovering by interactionSource.collectIsFocusedAsState() //if hovered

    val Inter = FontFamily(
        Font(R.font.inter_light,FontWeight.Light),
        Font(R.font.inter_regular,FontWeight.Normal),
        Font(R.font.inter_medium,FontWeight.Medium),
        Font(R.font.inter_semibold,FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold)
    )

    Button(
        interactionSource = interactionSource,
        onClick = {
            if (onClick != null) {
                onClick()
            }
        },
        shape = RoundedCornerShape(50.dp),
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            //.hoverable(interactionSource = interactionSource, enabled = true)
            .height(54.dp)
            .indication(interactionSource, rememberRipple(bounded = true, color = Color.Black))

        ,
        contentPadding= PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = Color(0xFF9FA2A5),
            disabledContentColor = Color(0xFFDCDCDC) ,//if(primary) (if(isPressed) positive else warning ) else blue,
            contentColor = Color.Black,
            backgroundColor = Color.White
        )
    ) {

        Row(
            horizontalArrangement =  Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){

            Text(text=text, color= if(enabled) Color.Black else Color(0xFFDCDCDC), fontWeight = FontWeight.SemiBold, fontSize = 18.sp, fontFamily = Inter)

        }

    }

}

@Composable
@Preview
fun PreviewButton() {
    NftCreatorTheme {
        ethOSButton(text ="Mint", enabled = true)
    }
}