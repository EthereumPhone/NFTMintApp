package org.ethereumphone.nftcreator.ui.screens

import android.net.Uri
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.ramcosta.composedestinations.annotation.Destination
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.moduls.TokenData
import org.ethereumphone.nftcreator.ui.components.DropDownSelector
import org.ethereumphone.nftcreator.ui.components.ImageBox
import org.ethereumphone.nftcreator.ui.components.InputField
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.ui.theme.md_theme_light_primary
import org.ethereumphone.nftcreator.utils.ContractInteraction
import org.ethereumphone.nftcreator.utils.WalletSDK
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

// https://www.youtube.com/watch?v=8waTylS0wUc

@ExperimentalComposeUiApi
@Composable
@Destination(start = true)
fun MintingScreen(
){
    Scaffold{
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
        ) {imageUri.value = it}
        ImageBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .clickable {
                    // Get image
                    launcher.launch("image/*")
                }
        )

        InputField(
            label = "Title",
            modifier = Modifier.fillMaxWidth(),
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
                .fillMaxHeight(.4f)
        ) {
            descriptionText = it
        }

        DropDownSelector(
            label = if (selectedNetwork == "") "Network" else selectedNetwork,
            options = options,
        ) {
            selectedNetwork = it
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = md_theme_light_primary),
                modifier = Modifier.fillMaxWidth(),
                enabled = imageUri.value != null,
                onClick = {
                    // Lets mint
                    // Upload the image on ipfs
                    val ipfs = IPFSApi()
                    val filename = "${System.currentTimeMillis()}.jpg"
                    val file = File(con.cacheDir, filename)
                    val fos = FileOutputStream(file)
                    val imageArray = con.contentResolver.openInputStream(imageUri.value!!)?.readBytes()!!
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
                        val jsonString = gson.toJson(TokenData(
                            name = titleText,
                            description = descriptionText,
                            image = imageIPFSHash
                        ))
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
                            tokenURI = jsonIPFSHash
                        )
                        Toast.makeText(con, "Minted!", Toast.LENGTH_LONG).show()

                    } else if (selectedNetwork.equals("IMX")) {
                        // Mint on IMX

                    }
                }
            ) {
                Text(text = "Mint")
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