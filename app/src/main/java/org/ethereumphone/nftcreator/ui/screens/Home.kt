package org.ethereumphone.nftcreator.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.pinkroom.walletconnectkit.WalletConnectKit
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.utils.ImxSigner
import org.ethereumphone.nftcreator.utils.ImxStarkSinger
import org.ethereumphone.nftcreator.utils.mintingWorkFlow
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label",text)
    clipboard.setPrimaryClip(clip)
}


@ExperimentalComposeUiApi
@Destination
@Composable
fun Home(
    navController: DestinationsNavigator?,
    walletConnectKit: WalletConnectKit?
) {
    val imageUri = remember { mutableStateOf<Uri?>(null)}
    val processing = remember {mutableStateOf(false)}


    NftCreatorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {
                    Column(Modifier.padding(15.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_ters),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                        )
                    }
                }
            }
        ) {
            Column {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) {imageUri.value = it}
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                            .padding(
                                PaddingValues(
                                    top = 20.dp,
                                    start = 30.dp,
                                    end = 30.dp,
                                    bottom = 22.dp
                                )
                            )
                            .border(
                                BorderStroke(5.dp, colorResource(id = R.color.purple_200)),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                launcher.launch("image/*")
                            }
                    ) {


                        if(imageUri.value != null) {
                            AsyncImage(
                                model = imageUri.value,
                                contentDescription = "Selected image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_tap_to_select_image),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                            )
                        }
                        if(processing.value) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    val con = LocalContext.current


                    // TODO: Fix custom theme
                    MaterialTheme {
                        Button(
                            enabled = imageUri.value != null,
                            onClick = {
                                processing.value = true

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
                                val signer = ImxSigner(walletConnectKit!!)
                                var starkSinger = ImxStarkSinger(signer)

                                mintingWorkFlow(
                                    signer = signer,
                                    starkSinger = starkSinger,
                                    ipfsHash = ipfsHash, //"QmSn5Y8cAxokNbJdqE91BDF7zQpNHw9VmNfmijzC3gQsTV",
                                    blueprint = ""
                                ).whenComplete { result, _ ->
                                    Log.d("test", result.toString())
                                    val jsonObject = JSONObject(result.toString())
                                    val dataObject: JSONObject= jsonObject.getJSONArray("results").get(0) as JSONObject
                                    val url = "https://market.sandbox.immutable.com/inventory/${dataObject.get("contract_address")}/${dataObject.get("token_id")}"
                                    con.copyToClipboard(url)
                                    Toast.makeText(con, "Copied URL to clipboard", Toast.LENGTH_LONG).show()
                                    processing.value = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(50),
                            // TODO: Fix theme
                            colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.Gray)
                        ) {
                            Text(
                                text = "MINT",
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                color = Color.White
                            )
                        }
                    }

                }
            }
        }

    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview(showBackground = false)
fun HomePreview() {
    Home(null,null)
}
