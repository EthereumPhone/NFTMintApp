package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import org.ethereumphone.nftcreator.ui.components.OverlappingTriangles
import org.ethereumphone.nftcreator.ui.components.TersLogo
import org.ethereumphone.nftcreator.ui.components.WalletConnectKitButton
import org.ethereumphone.nftcreator.ui.screens.destinations.HomeDestination

@ExperimentalComposeUiApi
@Destination(start = true)
@Composable
fun Login(
    navController: DestinationsNavigator?,
    walletConnectKit: WalletConnectKit // jetpack-destination dependency
) {
    Scaffold {
        Column{
            TersLogo()
            OverlappingTriangles()
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                WalletConnectKitButton(walletConnectKit = walletConnectKit, onConnected = {
                    navController!!.navigate(
                        HomeDestination()
                    )
                })
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

    Login(null ,walletConnectKit)
}