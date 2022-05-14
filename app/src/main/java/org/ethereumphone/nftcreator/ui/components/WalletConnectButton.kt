package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun WalletConnectButton(
    onClick: () -> Unit,
) {
    MaterialTheme {
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Text("Connect Wallet")
        }
    }
}



@Composable
@Preview(showBackground = false)
fun WalletConnectButtonPreview() {
    WalletConnectButton(onClick = {})
}


