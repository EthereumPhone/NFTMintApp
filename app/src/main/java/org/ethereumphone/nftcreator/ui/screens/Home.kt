package org.ethereumphone.nftcreator.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.ethereumphone.nftcreator.R

@Composable
fun Home(
    address: String,
) {

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
        Column {
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
                val imageUri = remember { mutableStateOf<Uri?>(null)}
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) {imageUri.value = it}

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .padding(
                            PaddingValues(
                                top = 30.dp,
                                start = 30.dp,
                                end = 30.dp,
                                bottom = 20.dp
                            )
                        )
                        .border(
                            BorderStroke(5.dp, colorResource(id = R.color.purple_200)),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            launcher.launch("image/*")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri.value),
                        contentDescription = "selected image"
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

                ) {
                Button(
                    onClick = {

                    },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    shape = RoundedCornerShape(50),

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
    Home("")
}
