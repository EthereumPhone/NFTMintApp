package org.ethereumphone.nftcreator.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import android.net.ConnectivityManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.moduls.MinterAttribute
import org.ethereumphone.nftcreator.moduls.Network
import org.ethereumphone.nftcreator.moduls.TokenData
import org.ethereumphone.nftcreator.ui.components.*
import org.ethereumphone.nftcreator.ui.theme.*
import org.ethereumphone.nftcreator.utils.*
import org.ethereumphone.nftcreator.utils.mintingWorkFlow
import org.ethereumphone.walletsdk.WalletSDK
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter


// https://www.youtube.com/watch?v=8waTylS0wUc

@ExperimentalComposeUiApi
@Composable
@Destination(start = true)
fun MintingScreen(
    imageUri: Uri?,
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
            MintingScreenInput(
                initalImageUri = imageUri
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MintingScreenInput(
    modifier: Modifier = Modifier,
    initalImageUri: Uri?
) {

    //For Snackbar
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }
    val sdeg = rememberSnackbarDelegate(hostState,scope)


    val options = listOf("Mainnet",
        //"Optimism",
        //"Arbitrum",
        "Goerli Testnet",
        //"Arbitrum Görlit Testnet"
    )
    var price = ""
    val mainnetNetwork = Network(
        chainName = "Mainnet",
        chainId = 1,
        chainExplorer = "https://etherscan.io",
        chainRPC = "https://cloudflare-eth.com",
        contractAddress = "0x2c6c5bc10af349574af2ee08c26f8aad185b3e3a"
    )
    val optimismNetwork = Network(
        chainName = "Optimism",
        chainId = 10,
        chainExplorer = "https://optimistic.etherscan.io",
        chainRPC = "https://mainnet.optimism.io",
        contractAddress = "0xf5b335fe4099ad0b005d8a09a864b4b3b3b5c5e1"
    )
    val arbitrumNetwork = Network(
        chainName = "Arbitrum",
        chainId = 42161,
        chainExplorer = "https://arbiscan.io",
        chainRPC = "https://arb1.arbitrum.io/rpc",
        contractAddress = "0xb084391C0d4af65297d9BB97C7e9C309A962907d"
    )
    val goerliNetwork = Network(
        chainName = "Goerli Testnet",
        chainId = 5,
        chainExplorer = "https://goerli.etherscan.io",
        chainRPC = "https://eth-goerli.public.blastapi.io",
        contractAddress = "0x5B48267F7fDb98416C8382C230f4f4AD7453aBd7"
    )
    val arbitrumGoerliNetwork = Network(
        chainName = "Arbitrum Görlit Testnet",
        chainId = 421613,
        chainExplorer = "https://goerli.arbiscan.io",
        chainRPC = "https://goerli-rollup.arbitrum.io/rpc",
        contractAddress = "0x5c4AA72b2847b3049e837b2fF218a696d9F50F50"
    )
    var selectedNetwork = mainnetNetwork
    val imageUri = remember { mutableStateOf<Uri?>(initalImageUri) }
    val con = LocalContext.current
    var titleText = ""
    var descriptionText = ""
    val processing = remember { mutableStateOf(false) }
    val processText = remember { mutableStateOf("Uploading image...") }
    val openCamOrGallery = remember { mutableStateOf(false) }

    val Inter = FontFamily(
        Font(R.font.inter_light,FontWeight.Light),
        Font(R.font.inter_regular,FontWeight.Normal),
        Font(R.font.inter_medium,FontWeight.Medium),
        Font(R.font.inter_semibold,FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold)
    )

    val showInfoDialog =  remember { mutableStateOf(false) }
    if(showInfoDialog.value){
        InfoDialog(
            setShowDialog = {
                showInfoDialog.value = false
            },
            title = "Disclaimers",
            text = "ethOS allows you to mint jpgs on Ethereum, " +
                    "but it assumes no ownership, liability, or " +
                    "responsibility for their use."

        )
    }

//    val walletSDK = WalletSDK(LocalContext.current)//access to wallet
//    val walletAddress = walletSDK.getAddress()//get wallet address
//    val context = LocalContext.current
//    val runnable = Runnable {
//        synchronized(context) {
//            val currChainid = walletSDK.getChainId()
//            if (currChainid != 1) {
//                println("Not on mainnet, changing chain")
//                walletSDK.changeChainid(1).get()
//            }
//        }
//    }
//    Thread(runnable).start()


    Box (
        modifier = modifier
            .fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Black)
                //.height(LocalConfiguration.current.screenHeightDp.dp)
                .padding(24.dp, 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                //modifier = Modifier.padding(top=4.dp)
            ) {

                TopHeader(title = "Mint", trailIcon = true, onClick={
                    showInfoDialog.value = true
                })
            }



            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight().padding(vertical = 18.dp)
            ) {
                //Image
                Box (
                    contentAlignment= Alignment.Center,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ){
                    val launcher = rememberLauncherForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            // Handle the result here
                            if (result.data?.data == null) {
                                val bitmap = result.data?.extras?.get("data") as Bitmap
                                val uri = getImageUri(con, bitmap)
                                imageUri.value = uri
                            } else {
                                val data: Intent? = result.data
                                imageUri.value = data?.data
                            }
                        }
                    }
                    if (openCamOrGallery.value) {

                        SelectDialog(
                            color = darkblue1,//Color(0xFF1E2730),
                            content = {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        contentPadding = PaddingValues(14.dp,0.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(0xFF3C4958),//Color(0xFF1E2730),//0xFF24303D),//0xFF3C4958),//
                                            contentColor = Color.White
                                        ),
                                        enabled = true,
                                        shape = CircleShape,

                                        onClick = {
                                            // Launch the camera intent
                                            openCamOrGallery.value = false
                                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                            launcher.launch(intent)
                                        }) {
                                        Text(text="Camera", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = Inter)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Button(
                                        contentPadding = PaddingValues(14.dp,0.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color(0xFF3C4958),//1E2730),
                                            contentColor = Color.White
                                        ),
                                        enabled = true,
                                        shape = CircleShape,

                                        onClick = {
                                            // Launch the gallery intent
                                            openCamOrGallery.value = false
                                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                            launcher.launch(intent)
                                        }) {
                                        Text(text="Gallery", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = Inter)

                                    }
                                }
                            },
                            title = "Choose Image Source",
                            setShowDialog = {
                                openCamOrGallery.value = false
                            }

                        )


                    }
                    if (imageUri.value != null) {
                        Box (
                            modifier =  Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                ,
                            contentAlignment = Alignment.Center
                        ){
                            AsyncImage(
                                model = imageUri.value,
                                contentDescription = "Selected image",
                                contentScale = ContentScale.Crop,

                                modifier =  Modifier.fillMaxSize()
                                    .clickable {
                                        openCamOrGallery.value = true
                                    },


                                )
                        }
                        //Image selected
                        sdeg.coroutineScope.launch {
                            sdeg.showSnackbar(SnackbarState.SUCCESS,"Image selected")
                        }

                    } else {
                        ImageBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clickable {
                                    // Get image
                                    openCamOrGallery.value = true
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                //Inputs
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.background(Color.Red)

                ) {

                    //Inputs & Button
                    InputField(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Enter Title",
                        value = "",
                    ) {
                        titleText = it
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    InputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f),
                        singeLine = false,

                        placeholder = "Enter Description",
                        value = ""
                    ) {
                        descriptionText = it
                    }





                }

                //Haptics
                var timings = longArrayOf(
                    25,25,25,25,25,200,25,25,25,25,25,25,25,25
                )
                var amplitudes = intArrayOf(
                    0,20,40,60,80,100,80,60,50,40,30,20,10,0
                )
                val repeatIndex = -1 // Do not repeat.

                var vibrator =  LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if(processing.value){


                    //Opacity Animation
                    val transition = rememberInfiniteTransition()
                    val fadingAnimation by transition.animateFloat(
                        initialValue = 1.0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 2000
                                1.0f at  0 with LinearEasing
                                0f at  1000 with LinearEasing
                                1.0f at  2000 with LinearEasing
                            }
                        )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Inputs & Button
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            modifier = Modifier.alpha(fadingAnimation),
                            text = processText.value,color= white)
                    }
                }
                else{

                    ethOSButton(
                        text ="Mint",

                        enabled = imageUri.value != null,

                        onClick = {
                            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex))

                            // Check if phone has internet connection
                            if (!isNetworkAvailable(con)) {
                                //launches snackbar component with couroutine
                                sdeg.coroutineScope.launch {
                                    sdeg.showSnackbar(SnackbarState.WARNING,"No internet connection")
                                }
                                return@ethOSButton
                            }
                            // Lets mint
                            // Upload the image on ipfs
                            processing.value = true
                            val runnable = Runnable {
                                val wallet = WalletSDK(con)
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
                                if (!selectedNetwork.equals(null)) {
                                    // Mint on evm just with different contracts
                                    // First the tokenJSON to IPFS
                                    var gson = Gson()
                                    val jsonString = gson.toJson(
                                        TokenData(
                                            name = titleText,
                                            description = descriptionText,
                                            image = "ipfs://$imageIPFSHash",
                                            attributes = listOf(
                                                MinterAttribute(
                                                    trait_type = "Minter",
                                                    value = wallet.getAddress()
                                                )
                                            )
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
                                        selectedNetwork = selectedNetwork
                                    )
                                    contractsIntercation.load()
                                    processText.value = "Minting NFT..."
                                    contractsIntercation.mintImage(
                                        address = wallet.getAddress(),
                                        tokenURI = "ipfs://$jsonIPFSHash"
                                    ).whenComplete { s, throwable ->
                                        processing.value = false
                                        if (s != WalletSDK.DECLINE) {
                                            imageUri.value = null
                                            val url = "${selectedNetwork.chainExplorer}/tx/$s"
                                            con.copyToClipboard(url)
                                            val uri = Uri.parse(url)
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            Thread.sleep(1000)
                                            con.startActivity(intent)

//                                                sdeg.coroutineScope.launch {
//                                                    sdeg.showSnackbar(SnackbarState.SUCCESS,"Minting Complete")
//                                                }
                                        }
                                    }

                                } else if (selectedNetwork.equals("IMX")) {
                                    // Mint on IMX
                                    val signer = ImxSigner(context = con)
                                    var starkSinger = ImxStarkSinger(signer, con.getSharedPreferences("STARK", Context.MODE_PRIVATE))

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
                    )
                }
            }
        }

        //Host for Snackbar

        ethOSSnackbarHost(delegate = sdeg, modifier = Modifier.padding(12.dp).align(alignment = Alignment.BottomStart))


    }





}

private fun getImageUri(context: Context, bitmap: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
    return Uri.parse(path)
}

fun isNetworkAvailable(con: Context): Boolean {
    val connectivityManager =
        con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
}



@ExperimentalComposeUiApi
@Composable
@Preview(showBackground = true)
fun PreviewMintingScreen() {
    NftCreatorTheme {

//        Column(
//            modifier = Modifier
////                .statusBarsPadding()
////                .navigationBarsPadding()
////                .imePadding()
//                .verticalScroll(rememberScrollState())
//                .fillMaxHeight()
//                .background(Green)
//        ) {


            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Green)
            ){
                MintingScreenInput(
                initalImageUri = null
            )
            }
//


//        }
    }
}