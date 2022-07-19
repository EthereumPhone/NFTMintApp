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
import androidx.core.content.res.ResourcesCompat.getColor
import org.ethereumphone.nftcreator.R

@ExperimentalComposeUiApi
@Composable
fun SplashScreen() {
     Scaffold(
        backgroundColor = colorResource(id = R.color.gray_dark),
         bottomBar = { BottomAppBar(backgroundColor = colorResource(id = R.color.gray_dark))
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_powered_by_ethos),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxWidth())
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
                painter = painterResource(id = R.drawable.ic_powered_by_ethos),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxHeight(0.1f)

            )


        }
    }
}




@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
fun SplashScreenPreview() {
    SplashScreen()
}