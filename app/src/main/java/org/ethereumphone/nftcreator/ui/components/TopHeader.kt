package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    title: String,
    onlyTitle: Boolean = true,
    trailIcon: Boolean = false,
    onClick: () -> Unit = {},
    imageVector: ImageVector = Icons.Outlined.Info
){


    Row (
        modifier = modifier
            .fillMaxWidth()
        ,//.padding(bottom=24.dp),
        //.background(Color.Red),
        horizontalArrangement = if(onlyTitle) Arrangement.Center else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){

        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .width(if(onlyTitle) 10.dp else 100.dp)
        ) {

            IconButton(
                onClick = {
                    if (!onlyTitle){
                        onBackClick()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Go back",
                    tint =  if (!onlyTitle) Color.White else Color.Transparent
                )
            }
        }

        //Header title
        Text(
            modifier = modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = title,
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )

        //Warning or info

        Row (
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .width(if(onlyTitle) 10.dp else 100.dp)
        ) {

            if (trailIcon){
                IconButton(
                    onClick = {
//                        if (!onlyTitle){
//                            onClick()
//                        }
                    },
                    modifier  = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "Go back",
                        tint =  Color.Transparent,
                        modifier  = Modifier.size(32.dp)
                    )
                }
            }

        }
    }

}