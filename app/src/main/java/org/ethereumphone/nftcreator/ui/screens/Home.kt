package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.ethereumphone.nftcreator.R

@Composable
fun Home() {
    Scaffold(
        backgroundColor = colorResource(id = R.color.gray_dark)
    ) {
        Column() {
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ters),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier

                )
            }
        }
    }
}



@Composable
@Preview(showBackground = false)
fun HomePreview() {
    Home()
}
