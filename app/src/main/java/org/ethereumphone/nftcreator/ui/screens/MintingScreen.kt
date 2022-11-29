package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import org.ethereumphone.nftcreator.ui.components.DropDownSelector
import org.ethereumphone.nftcreator.ui.components.ImageBox
import org.ethereumphone.nftcreator.ui.components.InputField
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.ui.theme.md_theme_light_primary

// https://www.youtube.com/watch?v=8waTylS0wUc

@ExperimentalComposeUiApi
@Composable
@Destination(start = true)
fun MintingScreen(
){
    Scaffold{
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            MintingScreenInput()
        }
    }
}

@Composable
fun MintingScreenInput(
    modifier: Modifier = Modifier
) {
    val options = listOf("Main", "Goerli", "IMX")
    var price = ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp),
        modifier = modifier
            .height(LocalConfiguration.current.screenHeightDp.dp)
            .padding(40.dp)

    ) {
        Text(
            text = "Mint new NFT",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
        )

        ImageBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
        )

        InputField(
            label = "Title",
            modifier = Modifier.fillMaxWidth()
        )

        InputField(
            label = "Description",
            singeLine = false,
            maxLines = 5,
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.4f)
        )

        DropDownSelector(
            label = "Network",
            options = options
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = md_theme_light_primary),
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                }
            ) {
                Text(text = "Mint for $price")
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
@Preview
fun PreviewMintingScreen() {
    NftCreatorTheme {
        MintingScreen()
    }
}