package org.ethereumphone.nftcreator.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Preview
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.ethereumphone.nftcreator.IPFSApi
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.moduls.MinterAttribute
import org.ethereumphone.nftcreator.moduls.Network
import org.ethereumphone.nftcreator.moduls.PreferencesHelper
import org.ethereumphone.nftcreator.moduls.TokenData
import org.ethereumphone.nftcreator.ui.components.*
import org.ethereumphone.nftcreator.ui.theme.*
import org.ethereumphone.nftcreator.utils.*
import org.ethereumphone.walletsdk.WalletSDK
import org.ethosmobile.components.library.core.dashedBorder
import org.ethosmobile.components.library.core.ethOSButton
import org.ethosmobile.components.library.core.ethOSHeader
import org.ethosmobile.components.library.core.ethOSInfoDialog
import org.ethosmobile.components.library.core.ethOSOnboardingModalBottomSheet
import org.ethosmobile.components.library.core.ethOSSelectDialog
import org.ethosmobile.components.library.mint.ethOSInputField
import org.ethosmobile.components.library.models.OnboardingItem
import org.ethosmobile.components.library.models.OnboardingObject
import org.ethosmobile.components.library.theme.Colors
import org.ethosmobile.components.library.theme.Fonts
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter


// https://www.youtube.com/watch?v=8waTylS0wUc

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                //.imePadding()
                .fillMaxSize()
        ) {
            MintingScreenInput(
                initalImageUri = imageUri
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    var titleText by remember {
        mutableStateOf("")
    }
    var descriptionText by remember {
        mutableStateOf("")
    }
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
        ethOSInfoDialog(
            setShowDialog = {
                showInfoDialog.value = false
            },
            title = "Disclaimers",
            text = "ethOS allows you to mint jpgs on Ethereum, " +
                    "but it assumes no ownership, liability, or " +
                    "responsibility for their use."

        )
    }

    val walletSDK = WalletSDK(LocalContext.current)//access to wallet
    val walletAddress = walletSDK.getAddress()//get wallet address
    val context = LocalContext.current
    val runnable = Runnable {
        synchronized(context) {
            val currChainid = walletSDK.getChainId()
            if (currChainid != 1) {
                println("Not on mainnet, changing chain")
                walletSDK.changeChainid(1).get()
            }
        }
    }
    Thread(runnable).start()

    val scrollState = rememberScrollState()


    //onboarding
    val modalSheetState = rememberModalBottomSheetState(true)

    // Minted
    val mintModalSheetState = rememberModalBottomSheetState(true)
    var mintedTxState by remember {
        mutableStateOf(false)
    }
    var currentTxState by remember {
        mutableStateOf("")
    }

    var keyNum by remember { mutableStateOf(0) }


    var preferenceValue by remember { mutableStateOf("") }
    // Load preference
    LaunchedEffect(key1 = Unit) {
        preferenceValue = PreferencesHelper.getPreference(context, "onboarding_key", "onboarding_uncomplete")
    }

    if (mintedTxState) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    mintModalSheetState.hide()
                }.invokeOnCompletion {

                }
                mintedTxState = false
                currentTxState = ""
            },
            sheetState = mintModalSheetState,
            containerColor = Color.Black,
            contentColor = Color.White
        ) {
            PendingTransactionStateUi(transactionHash = currentTxState)
        }
    }

    if(preferenceValue == "onboarding_uncomplete"){
        ethOSOnboardingModalBottomSheet(
            onDismiss = {
                scope.launch {
                    modalSheetState.hide()
                }.invokeOnCompletion {

                }
                PreferencesHelper.setPreference(context, "onboarding_key", "onboarding_complete")
                preferenceValue = "onboarding_complete"
            },
            sheetState = modalSheetState,
            onboardingObject = OnboardingObject(
                imageVector = R.drawable.mint,
                title = "Mint",
                items = listOf(
                    OnboardingItem(
                        imageVector = Icons.Rounded.Upload,
                        title = "Mint",
                        subtitle = "Mint NFTs straight to Ethereum straight from your camera roll with only a few clicks."
                    ),
                    OnboardingItem(
                        imageVector = Icons.Rounded.Preview,
                        title = "Viewable on Opensea",
                        subtitle = "To view, open Opensea in Three and connect your system wallet."
                    ),
                    OnboardingItem(
                        imageVector = Icons.Outlined.ErrorOutline,
                        title = "Disclaimer",
                        subtitle = "ethOS does not take any ownership, liability or responsibility over what ethOS Mint is used for."
                    )
                )
            )
        )
    }

    Column(
        //verticalArrangement = Arrangement.SpaceBetween,
        modifier  = Modifier.fillMaxSize()
    ) {
        ethOSHeader(title = "Mint", isTrailContent = true, trailContent = {
            IconButton(
                onClick = {
                    showInfoDialog.value = true
                },
                modifier  = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Go back",
                    tint =  Colors.GRAY,
                    modifier  = Modifier.size(32.dp)
                )
            }
        })

       Column (
           verticalArrangement = Arrangement.SpaceBetween,
           modifier = modifier
               .fillMaxSize()
               .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 24.dp,)
       ){
           Column(
               horizontalAlignment = Alignment.CenterHorizontally,
               //verticalArrangement = Arrangement.SpaceBetween,
               modifier = modifier
                   .scrollable(scrollState, Orientation.Vertical)
                   //.verticalFadingEdge(scrollState,32.dp,Colors.BLACK)
               //.height(LocalConfiguration.current.screenHeightDp.dp)
               //.padding(horizontal = 24.dp)
           ) {

//                item {
               Column(
                   modifier = Modifier
               ) {
                   //Image
                   Box(
                       contentAlignment = Alignment.Center,
                       modifier = Modifier.clip(RoundedCornerShape(12.dp))
                   ) {
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

                           ethOSSelectDialog(
                               title = "Select",
                               color = Colors.BLACK,
                               setShowDialog = { openCamOrGallery.value = false },
                               firstOptionTitle = "Camera",
                               firstOptionIcon = Icons.Outlined.CameraAlt,
                               onClickFirstOption = {
                                   // Launch the camera intent
                                   openCamOrGallery.value = false
                                   val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                   launcher.launch(intent)
                               },
                               secondOptionTitle = "Gallery",
                               secondOptionIcon = Icons.Outlined.Image,
                               onClickSecondOption = {
                                   // Launch the gallery intent
                                   openCamOrGallery.value = false
                                   val intent = Intent(
                                       Intent.ACTION_PICK,
                                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                   )
                                   launcher.launch(intent)
                               },
                           )


                       }
                       if (imageUri.value != null) {
                           Box(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(250.dp),
                               contentAlignment = Alignment.Center
                           ) {
                               AsyncImage(
                                   model = imageUri.value,
                                   contentDescription = "Selected image",
                                   contentScale = ContentScale.Crop,

                                   modifier = Modifier
                                       .fillMaxSize()
                                       .clickable {
                                           openCamOrGallery.value = true
                                       },


                                   )
                           }

                       } else {
                           ethOSImageBox(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(250.dp),
                               onClick = {// Get image
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
                       //modifier = Modifier.fadingEdge(topBottomFade),
                   ) {

                       key(keyNum) {
                           //Inputs & Button
                           ethOSInputField(
                               modifier = Modifier.fillMaxWidth(),
                               placeholder = "Enter title",
                               maxLines = 2,
                               singeLine = false,
                               size = 32,
                               value = "",
                           ) {
                               titleText = it
                           }
                           Spacer(modifier = Modifier.height(8.dp))
                           ethOSInputField(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .fillMaxHeight(0.6f)
                                   //.verticalFadingEdge(scrollState,12.dp,Colors.BLACK),
                                   .fadingEdge(topBottomFade),
                               singeLine = false,

                               placeholder = "Enter description",
                               value = descriptionText
                           ) {
                               descriptionText = it
                           }
                       }
                   }
               }

           }

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
                   horizontalAlignment = Alignment.CenterHorizontally,
                   modifier = modifier.fillMaxWidth()
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

               //Haptics
               var timings = longArrayOf(
                   25,25,25,25,25,200,25,25,25,25,25,25,25,25
               )
               var amplitudes = intArrayOf(
                   0,20,40,60,80,100,80,60,50,40,30,20,10,0
               )
               val repeatIndex = -1 // Do not repeat.

               var vibrator =  LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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
                                       Thread.sleep(1000)
                                       imageUri.value = null
                                       titleText = ""
                                       descriptionText = ""
                                       keyNum += 1
                                   }
                                   currentTxState = s
                                   mintedTxState = true
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







}

@Composable
fun ethOSImageBox(
    modifier: Modifier= Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .dashedBorder(
                4.dp,
                Colors.GRAY,
                shape= RoundedCornerShape(24.dp),
                on = 20.dp,
                off = 10.dp
            )
            .clickable {
                onClick()
            }


    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Outlined.Image,
                    contentDescription = "Select image",
                    tint = Colors.GRAY,
                    modifier = Modifier
                        .size(36.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Text(
                    text = "Select image",
                    fontSize = 18.sp,
                    color = Colors.GRAY,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Fonts.INTER
                )
            }
        }
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }
//
val topFade = Brush.verticalGradient(0f to Colors.TRANSPARENT, 0.05f to Colors.BLACK)
//Box(modifier = Modifier.fadingEdge(topFade).background(Color.Blue).size(100.dp))
val bottomFade = Brush.verticalGradient(0.8f to Colors.TRANSPARENT, 1f to Colors.BLACK)


//
val topBottomFade = Brush.verticalGradient(0f to Colors.TRANSPARENT, 0.1f to Colors.BLACK, 0.7f to Colors.BLACK, 1f to Colors.TRANSPARENT)
//Box(modifier = Modifier.fadingEdge(topBottomFade).background(Color.Blue).size(100.dp))
//
//val leftRightFade = Brush.horizontalGradient(0f to Colors.TRANSPARENT, 0.1f to Colors.ERROR, 0.9f to Colors.ERROR, 1f to Colors.TRANSPARENT)
//Box(modifier = Modifier.fadingEdge(leftRightFade).background(Color.Blue).size(100.dp))
//
//val radialFade = Brush.radialGradient(0f to Color.Red, 0.5f to Color.Transparent)
//Box(modifier = Modifier.fadingEdge(radialFade).background(Color.Blue).size(100.dp))

fun Modifier.verticalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color? = null,
) = composed(
    debugInspectorInfo {
        name = "length"
        value = length
    }
) {
    val color = edgeColor ?: MaterialTheme.colors.surface

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromTop = scrollState.value
        val scrollFromBottom = scrollState.maxValue - scrollState.value

        val topFadingEdgeStrength = lengthValue * (scrollFromTop / lengthValue).coerceAtMost(1f)

        val bottomFadingEdgeStrength = lengthValue * (scrollFromBottom / lengthValue).coerceAtMost(1f)

        drawContent()

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Colors.TRANSPARENT,
                ) as List<androidx.compose.ui.graphics.Color>,
                startY = 0f,
                endY = topFadingEdgeStrength,
            ),
            size = Size(
                this.size.width,
                topFadingEdgeStrength
            ),
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Colors.TRANSPARENT,
                    color,
                ),
                startY = size.height - bottomFadingEdgeStrength,
                endY = size.height,
            ),
            topLeft = Offset(x = 0f, y = size.height - bottomFadingEdgeStrength),
        )
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

@Composable
fun PendingTransactionStateUi(
    transactionHash: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 48.dp)
            .fillMaxHeight(.5f)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(transactionHash.startsWith("0x")) {
                Box (
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(110.dp)
                        .border(
                            5.dp, Colors.SUCCESS,
                            CircleShape
                        )
                ){
                    androidx.compose.material3.Icon(
                        Icons.Rounded.Check,
                        "Approve",
                        tint = Colors.SUCCESS,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                androidx.compose.material3.Text(
                    text = "Transaction Succeeded.",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.size(8.dp))

                androidx.compose.material3.Text(
                    text = "It might take some time for the NFT to be indexed by OpenSea.",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp) // Add padding here
                )

                androidx.compose.material3.Text(
                    text = "View NFT on OpenSea",
                    color = Color(0xFF71B5FF),
                    fontSize = 18.sp,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable {
                        // Create link for opensea
                        val link = "https://opensea.io/collection/ethos-minting-app-1?search[sortAscending]=false&search[sortBy]=CREATED_DATE"

                        // Open link in browser
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(link)
                        context.startActivity(intent)
                    }
                )

            }
            else {

                Box (
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(110.dp)
                        .border(
                            5.dp, Colors.ERROR,
                            CircleShape
                        )
                ){
                    androidx.compose.material3.Icon(
                        Icons.Rounded.Close,
                        "Failed",
                        tint = Colors.ERROR,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                androidx.compose.material3.Text(
                    text = "Transaction Failed",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = Fonts.INTER,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }


    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun PreviewMintingScreen() {
    /*
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box (
                modifier = Modifier
                    .fillMaxSize()

            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(Colors.BLACK)
                        //.height(LocalConfiguration.current.screenHeightDp.dp)
                        .padding(24.dp, 24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        //modifier = Modifier.padding(top=4.dp)
                    ) {

                        TopHeader(title = "Mint", trailIcon = true, onClick={
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


                            ImageBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clickable {
                                        // Get image
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        //Inputs
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,


                            ) {

                            //Inputs & Button
                            InputField(
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = "Enter Title",
                                value = "",
                            ) {
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

                            }





                        }

                        ethOSButton(
                            text ="Mint",

                            enabled = true,

                            onClick = {
                            }
                        )




                    }
                }


                //Host for Snackbar



            }

        }
    }
*/
    MintingScreenInput(initalImageUri=null)
}