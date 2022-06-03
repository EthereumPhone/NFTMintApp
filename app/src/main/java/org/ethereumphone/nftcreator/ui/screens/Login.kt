package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.components.TersLogo
import org.ethereumphone.nftcreator.ui.components.WalletConnectButton

@ExperimentalComposeUiApi
@Composable
fun Login(
    walletConnectKit: WalletConnectKit,
    onConnected: (address: String) -> Unit
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
        // main
        Column(
            Modifier.fillMaxHeight(),
            //verticalArrangement = Arrangement.
        ) {

            TersLogo()
            Text(
                "NFT Creator",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrows),
                contentDescription = "ters logo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                WalletConnectButton(walletConnectKit = walletConnectKit, onConnected = onConnected)
            }
        }
    }
}







@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
fun LoginPreview() {
    val config = WalletConnectKitConfig(
        context = LocalContext.current,
        bridgeUrl = "wss://bridge.aktionariat.com:8887",
        appUrl = "walletconnectkit.com",
        appName = "WalletConnect Kit",
        appDescription = "This is the Swiss Army toolkit for WalletConnect!"
    )
    val walletConnectKit = WalletConnectKit.Builder(config).build()
    Login(walletConnectKit, {})
}