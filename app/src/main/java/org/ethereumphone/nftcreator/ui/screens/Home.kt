package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ethereumphone.nftcreator.R

@Composable
fun Home() {
    Scaffold(
        backgroundColor = colorResource(id = R.color.gray_dark),
        bottomBar = { BottomAppBar(backgroundColor = colorResource(id = R.color.gray_dark))
            {
            Icon(
                painter = painterResource(id = R.drawable.ic_ethos),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth())
            }
        }
    ) {
        Column(
        ) {
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
                        .padding(top = 20.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BoxWithConstraints(

                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .padding(PaddingValues(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 10.dp))
                        .border(
                            BorderStroke(5.dp, colorResource(id = R.color.purple_200)),
                            shape = RectangleShape
                        ),
                ) {
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

                ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    shape = RoundedCornerShape(50)

                ) {
                    Text("Mint NFT")
                }
            }
        }
    }
}




@Composable
@Preview(showBackground = false)
fun HomePreview() {
    Home()
}
