package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.components.OverlappingTriangles
import org.ethereumphone.nftcreator.ui.components.TersLogo
import org.ethereumphone.nftcreator.ui.screens.destinations.HomeDestination
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.koin.androidx.compose.get

@ExperimentalComposeUiApi
@Destination(start = true)
@Composable
fun Login(
    navController: DestinationsNavigator,
    walletConnect: ConnectWalletViewModel //= get() // injected
) {
    val context = LocalContext.current
    val walletAddress = walletConnect.userWallet.collectAsState()

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
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        walletConnect.connectWallet(context) {
                            if(walletConnect.userWallet.value != "") {
                                navController.navigate(
                                    HomeDestination()
                                )
                            }
                        }
                    }
                ) {
                    Text(text = "connect Wallet")
                }
            }
        }
    }
}







@Composable
@ExperimentalComposeUiApi
@Preview(showBackground = true)
fun LoginPreview() {
    /*
    val config = WalletConnectKitConfig(
        context = LocalContext.current,
        bridgeUrl = "wss://bridge.aktionariat.com:8887",
        appUrl = "walletconnectkit.com",
        appName = "WalletConnect Kit",
        appDescription = "This is the Swiss Army toolkit for WalletConnect!"
    )
    val walletConnectKit = WalletConnectKit.Builder(config).build()
    */

    //Login(walletConnectKit, {})
}