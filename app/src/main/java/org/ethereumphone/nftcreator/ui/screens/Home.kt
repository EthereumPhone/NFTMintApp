package org.ethereumphone.nftcreator.ui.screens

import android.net.LinkAddress
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavDestinationDsl
import coil.compose.rememberAsyncImagePainter
import com.immutable.sdk.ImmutableXCore
import com.immutable.sdk.Signer
import com.immutable.sdk.StarkSigner
import com.immutable.sdk.api.CollectionsApi
import com.immutable.sdk.api.MetadataApi
import com.immutable.sdk.api.MintsApi
import com.immutable.sdk.api.model.*
import com.immutable.sdk.crypto.StarkKey
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.pinkroom.walletconnectkit.WalletConnectKit
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.screens.destinations.LoginDestination
import org.ethereumphone.nftcreator.utils.ImxSigner
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.koin.androidx.compose.get
import java.io.File
import kotlin.random.Random

@ExperimentalComposeUiApi
@Destination
@Composable
fun Home(
    navController: DestinationsNavigator,
    walletConnect: ConnectWalletViewModel,
    walletConnectKit: WalletConnectKit
) {
    val imageUri = remember { mutableStateOf<Uri?>(null)}



    Scaffold(
        backgroundColor = colorResource(id = R.color.gray_dark),
        bottomBar = { BottomAppBar(backgroundColor = colorResource(id = R.color.gray_dark))
            {
            Icon(
                painter = painterResource(id = R.drawable.ic_ters),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth())
            }
        }
    ) {
        Column {
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_powered_by_ethos),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(top = 20.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) {imageUri.value = it}
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .padding(
                            PaddingValues(
                                top = 30.dp,
                                start = 30.dp,
                                end = 30.dp,
                                bottom = 20.dp
                            )
                        )
                        .border(
                            BorderStroke(5.dp, colorResource(id = R.color.purple_200)),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            launcher.launch("image/*")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri.value),
                        contentDescription = "selected image"
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

                ) {

                val con = LocalContext.current
                Button(
                    onClick = {
                        val imageArray = con.contentResolver.openInputStream(imageUri.value!!)?.readBytes()!!
                        val ipfs = IPFSApi()
                        val ipfsHash = ipfs.uploadImage(imageArray)
                        Log.d("test", ipfsHash)

                        // get StarKey
                        StarkKey.generate(ImxSigner(walletConnect, context = con, walletConnectKit = walletConnectKit))
                            .whenComplete { keyPair, error ->

                                var authSignature = StarkKey.sign(keyPair, walletConnectKit.address!!)

                                // mintRequest
                                val mintTokenDataV2 = MintTokenDataV2(
                                    id = (1..1000000000).random().toString(), // wont work if same id
                                    blueprint = null,
                                    royalties = null
                                )

                                val mintUser =  MintUser(
                                    tokens = listOf(mintTokenDataV2),
                                    user = walletConnectKit.address!!

                                )

                                val mintRequest = MintRequest(
                                    authSignature = authSignature,
                                    contractAddress = "0xf3d64ec690E551F94ac3d4DcE5ce4Bd191466318",
                                    users = listOf(mintUser),
                                    royalties = null
                                )
                                //MetadataApi().

                                var response = MintsApi().mintTokens(listOf(mintRequest))

                                Log.d("response", response.results.toString())
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    shape = RoundedCornerShape(50),

                ) {
                    Text("Mint NFT")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun HomePreview() {
    //Home()
}
