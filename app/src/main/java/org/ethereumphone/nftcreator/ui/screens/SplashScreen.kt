package org.ethereumphone.nftcreator.ui.screens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat.getColor
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme

@ExperimentalComposeUiApi
@Composable
fun SplashScreen() {
    NftCreatorTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_powered_by_ethos),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .fillMaxSize(0.45f)
                        )
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_ters),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                )
            }
        }
    }



}




@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
fun SplashScreenPreview() {
    SplashScreen()
}