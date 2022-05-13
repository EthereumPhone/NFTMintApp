package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.pinkroom.walletconnectkit.WalletConnectButton


@Composable
fun WalletConnectButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    MaterialTheme {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues()

        ) {
            Text(text)
        }
    }
}



@Composable
@Preview(showBackground = false)
fun WalletConnectButtonPreview() {
    WalletConnectButton(text = "test", onClick = {})
}


