package org.ethereumphone.nftcreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.components.TersLogo
import org.ethereumphone.nftcreator.ui.screens.destinations.HomeDestination
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.koin.androidx.compose.get

@ExperimentalComposeUiApi
@Destination(start = true)
@Composable
fun Login(
    navController: DestinationsNavigator,
    walletConnect: ConnectWalletViewModel = get() // injected
) {
    val context = LocalContext.current

    Scaffold(
        //backgroundColor = MaterialTheme.colors.background,
        bottomBar = { BottomAppBar {
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
        ) {

            TersLogo()

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
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        walletConnect.connectWallet(context)

                        if(walletConnect.userWallet.value != "") {
                            navController.navigate(
                                HomeDestination(
                                    walletConnect.userWallet.value
                                )
                            )
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