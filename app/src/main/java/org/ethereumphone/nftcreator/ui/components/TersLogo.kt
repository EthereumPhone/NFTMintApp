package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.spacing


@Composable
fun TersLogo() {
    Column(
        Modifier
            .padding(top = 30.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "ters logo",
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxWidth(0.65F)
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
        Icon(
            painter = painterResource(id = R.drawable.ic_ters),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier

        )
        Text(
            "NFT Creator",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}






@Composable
@Preview(showBackground = false)
fun TersLogoPreview() {
    TersLogo()
}