package org.ethereumphone.nftcreator.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.ramcosta.composedestinations.annotation.Destination
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.moduls.TokenData
import org.ethereumphone.nftcreator.ui.components.DropDownSelector
import org.ethereumphone.nftcreator.ui.components.ImageBox
import org.ethereumphone.nftcreator.ui.components.InputField
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.ui.theme.md_theme_light_primary
import org.ethereumphone.nftcreator.utils.*
import org.ethereumphone.nftcreator.utils.mintingWorkFlow
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

// https://www.youtube.com/watch?v=8waTylS0wUc

@ExperimentalComposeUiApi
@Composable
@Destination(start = true)
fun MintingScreen(
) {
    Scaffold {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            MintingScreenInput()
        }
    }
}

@Composable
fun MintingScreenInput(
    modifier: Modifier = Modifier
) {
    val options = listOf("Mainnet", "Goerli Testnet", "IMX")
    var price = ""
    var selectedNetwork = ""
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val con = LocalContext.current
    var titleText = ""
    var descriptionText = ""
    val processing = remember { mutableStateOf(false) }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp),
        modifier = modifier
            .height(LocalConfiguration.current.screenHeightDp.dp)
            .padding(40.dp)

    ) {
        Text(
            text = "Mint new NFT",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { imageUri.value = it }
        if (imageUri.value != null) {
            AsyncImage(
                model = imageUri.value,
                contentDescription = "Selected image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        } else {
            ImageBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .clickable {
                        // Get image
                        launcher.launch("image/*")
                    }
            )
        }


        InputField(
            label = "Title",
            modifier = Modifier.fillMaxWidth(),
            value = ""
        ) {
            titleText = it
        }

        InputField(
            label = "Description",
            singeLine = false,
            maxLines = 5,
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.4f),
            value = ""
        ) {
            descriptionText = it
        }



        DropDownSelector(
            label = "Network",
            options = options,
        ) {
            selectedNetwork = it
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            if(processing.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(70.dp)
                        .fillMaxWidth()
                )
            } else {
                Button(
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = md_theme_light_primary),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = imageUri.value != null,
                    onClick = {
                        // Lets mint
                        // Upload the image on ipfs
                        processing.value = true
                        val runnable = Runnable {
                            val ipfs = IPFSApi()
                            val filename = "${System.currentTimeMillis()}.jpg"
                            val file = File(con.cacheDir, filename)
                            val fos = FileOutputStream(file)
                            val imageArray =
                                con.contentResolver.openInputStream(imageUri.value!!)?.readBytes()!!
                            try {
                                fos.write(imageArray);
                            } finally {
                                fos.close();
                            }
                            val imageIPFSHash = ipfs.uploadFile(file)
                            if (selectedNetwork.equals("Mainnet") || selectedNetwork.equals("Goerli Testnet")) {
                                // Mint on evm just with different contracts
                                // First the tokenJSON to IPFS
                                var gson = Gson()
                                val jsonString = gson.toJson(
                                    TokenData(
                                        name = titleText,
                                        description = descriptionText,
                                        image = "ipfs://$imageIPFSHash"
                                    )
                                )
                                val jsonFileName = "${System.currentTimeMillis()}.json"
                                val jsonFile = File(con.cacheDir, jsonFileName)
                                val fw = FileWriter(jsonFile)
                                try {
                                    fw.write(jsonString)
                                } finally {
                                    fw.close()
                                }

                                val jsonIPFSHash = ipfs.uploadFile(jsonFile)

                                val contractsIntercation = ContractInteraction(
                                    con = con,
                                    mainnet = (selectedNetwork == "Mainnet")
                                )
                                contractsIntercation.load()

                                contractsIntercation.mintImage(
                                    address = WalletSDK(con).getAddress(),
                                    tokenURI = "ipfs://$jsonIPFSHash"
                                ).whenComplete { s, throwable ->
                                    processing.value = false
                                    imageUri.value = null
                                    val url = if (selectedNetwork == "Mainnet") "https://etherscan.io/tx/$s" else "https://goerli.etherscan.io/tx/$s"
                                    con.copyToClipboard(url)
                                    val uri = Uri.parse(url)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    con.startActivity(intent)
                                }

                            } else if (selectedNetwork.equals("IMX")) {
                                // Mint on IMX
                                val signer = ImxSigner(context = con)
                                var starkSinger = ImxStarkSinger(signer)

                                mintingWorkFlow(
                                    signer = signer,
                                    starkSinger = starkSinger,
                                    ipfsHash = imageIPFSHash, //"QmSn5Y8cAxokNbJdqE91BDF7zQpNHw9VmNfmijzC3gQsTV",
                                    blueprint = ""
                                ).whenComplete { result, _ ->
                                    Log.d("test", result.toString())
                                    val jsonObject = JSONObject(result.toString())
                                    val dataObject: JSONObject =
                                        jsonObject.getJSONArray("results").get(0) as JSONObject
                                    val url =
                                        "https://market.sandbox.immutable.com/inventory/${dataObject.get("contract_address")}/${
                                            dataObject.get("token_id")
                                        }"
                                    con.copyToClipboard(url)
                                    Thread.sleep(1000)
                                    processing.value = false
                                    imageUri.value = null
                                    val uri = Uri.parse(url)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    con.startActivity(intent)
                                }
                            }
                        }
                        Thread(runnable).start()

                    }
                ) {
                    Text(text = "Mint")
                }
            }

        }
    }


}


@ExperimentalComposeUiApi
@Composable
@Preview
fun PreviewMintingScreen() {
    NftCreatorTheme {
        MintingScreen()
    }
}