package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

import org.walletconnect.Session
import androidx.lifecycle.viewModelScope
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import kotlinx.coroutines.launch


@Composable
fun WalletConnectKitButton(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier
        .size(232.dp, 48.dp)
        .padding(12.dp),
    walletConnectKit: WalletConnectKit,
    onConnected: (address: String) -> Unit,
    onDisconnected: (() -> Unit)? = null,
    sessionCallback: Session.Callback? = null
) {
    val viewModel = WCKViewModel(walletConnectKit, onConnected, onDisconnected, sessionCallback)
    viewModel.loadSessionIfStored()
    MaterialTheme {
        Button(
            //colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.design_default_color_background)),
            onClick = viewModel::onClick,
            contentPadding = PaddingValues(),
            modifier = modifier
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(20.dp),
            //border = BorderStroke(1.5.dp, colorResource(id = R.color.walletconnect_icon))
        ) {
            Text("Connect Wallet")
        }
    }
}


internal class WCKViewModel constructor(
    private val walletConnectKit: WalletConnectKit,
    private val onConnected: (address: String) -> Unit,
    private val onDisconnected: (() -> Unit)?,
    private val sessionCallback: Session.Callback?
) : ViewModel(), Session.Callback {

    override fun onStatus(status: Session.Status) {
        viewModelScope.launch {
            when (status) {
                is Session.Status.Approved -> onSessionApproved()
                is Session.Status.Connected -> onSessionConnected()
                is Session.Status.Closed -> onSessionDisconnected()
            }
            sessionCallback?.onStatus(status)
        }
    }

    override fun onMethodCall(call: Session.MethodCall) {
        viewModelScope.launch { sessionCallback?.onMethodCall(call) }
    }

    fun onClick() {
        if (walletConnectKit.isSessionStored) {
            walletConnectKit.removeSession()
        }
        walletConnectKit.createSession(this)
    }

    fun loadSessionIfStored() {
        if (walletConnectKit.isSessionStored) {
            walletConnectKit.loadSession(this)
            walletConnectKit.address?.let(onConnected)
        }
    }

    private fun onSessionApproved() {
        walletConnectKit.address?.let(onConnected)
    }

    private fun onSessionConnected() {
        walletConnectKit.address ?: walletConnectKit.requestHandshake()
    }

    private fun onSessionDisconnected() {
        onDisconnected?.invoke()
    }
}

@Preview
@Composable
private fun ComposablePreview() {
    val config = WalletConnectKitConfig(
        context = LocalContext.current,
        bridgeUrl = "wss://bridge.aktionariat.com:8887",
        appUrl = "walletconnectkit.com",
        appName = "WalletConnect Kit",
        appDescription = "This is the Swiss Army toolkit for WalletConnect!"
    )
    val walletConnectKit = WalletConnectKit.Builder(config).build()
    WalletConnectKitButton(
        modifier = Modifier.fillMaxWidth(),
        walletConnectKit = walletConnectKit,
        onConnected = {},
    )
}


