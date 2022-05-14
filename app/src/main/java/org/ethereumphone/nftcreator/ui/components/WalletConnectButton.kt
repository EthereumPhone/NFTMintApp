package org.ethereumphone.nftcreator.ui.components

import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.shape.ShapeAppearanceModel
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
            contentPadding = PaddingValues(
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)


        ) {
            Text(text)
        }
    }
}



@Composable
@Preview(showBackground = false)
fun WalletConnectButtonPreview() {
    WalletConnectButton(text = "connect wallet", onClick = {})
}


