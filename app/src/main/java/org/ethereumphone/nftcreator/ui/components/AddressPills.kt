package org.ethereumphone.nftcreator.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.ethereumphone.nftcreator.ui.screens.MintingScreen
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.ui.theme.gray


@Composable
fun AddressPills(
    address: String,
    chainId: Int,
    onclick: () -> Unit,
    icon: @Composable () -> Unit,
    sdeg:SnackbarDelegate
) {


    var network = ""
    when (chainId) {
        1 -> network = "Mainnet"
        5 -> network = "Goerli"
        10 -> network = "Optimism"
        137 -> network = "Polygon"
        42161 -> network = "Arbitrum"
        else -> {
            network = "Mainnet"
        }
    }

    var color = Color(0xFF2E3D4E)
    when (chainId) {
        1 -> color = Color(0xFF45BA4A)
        5 -> color = Color(0xFFFCA311)
        10 -> color = Color(0xFFE52222)
        137 -> color = Color(0xFF8A50DA)
        42161 -> color = Color(0xFF49A9F2)
        else -> {
            color = Color(0xFF45BA4A)
        }
    }

    val text = network+" " + address.take(5) + "..."
    val context = LocalContext.current


    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Address
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ){
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Information",
                    tint = Color.Transparent,
                    modifier = Modifier
                        .clip(CircleShape)
                    //.background(Color.Red)
                )
            }
            Box(
                modifier = Modifier
                    .clickable {
//                        val clipboard: ClipboardManager? =
//                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
//                        val clip = ClipData.newPlainText("Address", address)
//                        clipboard?.setPrimaryClip(clip)

//                        sdeg.coroutineScope.launch {
//                            sdeg.showSnackbar(SnackbarState.WARNING,"No internet connection")
//                        }
                               },
            ){
                Text(
                    modifier = Modifier
                        .clickable {
//                            val clipboard: ClipboardManager? =
//                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
//                            val clip = ClipData.newPlainText("Address", address)
//                            clipboard?.setPrimaryClip(clip)
                        },
                    text = address,//truncateText(address),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            icon()
        }
        //Networkpill
//        val chainName = when(chainId) {
//            1 -> "Mainnet"
//            5 -> "Goerli"
//            10 -> "Optimism"
//            137 -> "Polygon"
//            42161 -> "Arbitrum"
//            else -> "Mainnet"
//        }
        Surface (
            modifier = Modifier
                .clip(CircleShape),
            color = Color(0xFF24303D),
            contentColor = Color.White
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                text = network,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

private fun truncateText(text: String): String {
    if (text.length > 19) {
        return text.substring(0, 5) + "..." + text.takeLast(3) + " "
    }
    return text
}

@ExperimentalComposeUiApi
@Composable @Preview
fun PreviewMintingScreen() {
    ///NftCreatorTheme {
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    val sdeg = rememberSnackbarDelegate(hostState, scope)
    AddressPills(
        "0xefBABdeE59968641DC6E892e30C470c2b40157Cd",
        1,
        sdeg = sdeg,
        icon = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Information",
                    tint = Color(0xFF9FA2A5),
                    modifier = Modifier
                        .clip(CircleShape)
                    //.background(Color.Red)
                )
            }
        },
        onclick = {

        }
    )
    //}
}
