package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.ethereumphone.nftcreator.R


@Composable
fun TersLogo() {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "ters logo",
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxWidth(0.6F)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_ters),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier

        )
    }

}






@Composable
@Preview(showBackground = false)
fun TersLogoPreview() {

    TersLogo()
}