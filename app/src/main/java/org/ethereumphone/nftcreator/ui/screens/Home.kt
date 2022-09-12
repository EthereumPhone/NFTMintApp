package org.ethereumphone.nftcreator.ui.screens

import android.content.Intent
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
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.pinkroom.walletconnectkit.WalletConnectKit
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.utils.ImxSigner
import org.ethereumphone.nftcreator.utils.ImxStarkSinger
import org.ethereumphone.nftcreator.utils.mintingWorkFlow
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream


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


                        val ipfs = IPFSApi()
                        val filename = "${System.currentTimeMillis()}.jpg"
                        val file = File(con.cacheDir, filename)
                        val fos = FileOutputStream(file)
                        val imageArray = con.contentResolver.openInputStream(imageUri.value!!)?.readBytes()!!
                        try {
                            fos.write(imageArray);
                        }
                        finally {
                            fos.close();
                        }

                        val ipfsHash = ipfs.uploadFile(file)
                        Log.d("test", ipfsHash)


                        // IMX signers
                        val signer = ImxSigner(walletConnectKit)
                        var starkSinger = ImxStarkSinger(signer)

                        mintingWorkFlow(
                            signer = signer,
                            starkSinger = starkSinger,
                            ipfsHash = ipfsHash, //"QmSn5Y8cAxokNbJdqE91BDF7zQpNHw9VmNfmijzC3gQsTV",
                            blueprint = ""
                        ).whenComplete { result, _ ->
                            Log.d("test", result.toString())
                            val jsonObject = JSONObject(result.toString())
                            // https://market.ropsten.immutable.com/inventory/contractAddress/tokenID
                            val url = "https://market.ropsten.immutable.com/inventory/${jsonObject.get("contract_address")}/${jsonObject.get("token_id")}"
                            val uri = Uri.parse(url)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            // Verify that the intent will resolve to an activity
                            // Verify that the intent will resolve to an activity
                            if (intent.resolveActivity(con.getPackageManager()) != null) {
                                // Here we use an intent without a Chooser unlike the next example
                                con.startActivity(intent)
                            }
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
