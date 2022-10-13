package org.ethereumphone.nftcreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import com.immutable.sdk.ImmutableXBase
import com.immutable.sdk.ImmutableX
import com.immutable.sdk.ImmutableXHttpLoggingLevel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dev.pinkroom.walletconnectkit.WalletConnectKit
import org.ethereumphone.nftcreator.ui.screens.Home

import org.ethereumphone.nftcreator.ui.screens.NavGraphs
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmutableX(base = ImmutableXBase.Sandbox).setHttpLoggingLevel(level = ImmutableXHttpLoggingLevel.Body)

        setContent {
            NftCreatorTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    dependenciesContainerBuilder = {
                        dependency(get<WalletConnectKit>())
                    }
                )
            }
        }
    }
}


/*
package org.ethereumphone.nftcreator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import dev.pinkroom.walletconnectkit.WalletConnectButton
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {


    private var connectedToWallet: Boolean = false
    private var walletConnectButton: WalletConnectButton? = null
    private var walletConnectKit: WalletConnectKit? = null
    var chooseImageButton: Button? = null
    var imageArray: ByteArray? = null
    var ipfsApi: IPFSApi? = null
    var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ipfsApi = IPFSApi()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val scope = CoroutineScope(Dispatchers.IO)
                    var progressBar = findViewById<ProgressBar>(R.id.progressBar)
                    progressBar.visibility = View.VISIBLE
                    scope.launch {
                        uploadNFT(result)
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            }

        val config = WalletConnectKitConfig(
            context = this,
            bridgeUrl = "wss://bridge.aktionariat.com:8887",
            appUrl = "https://ethereumphone.org",
            appName = "NFT Creator",
            appDescription = "Create NFTs!"
        )

        walletConnectKit = WalletConnectKit.Builder(config).build()


        chooseImageButton = findViewById<Button>(R.id.chooseImage)

        chooseImageButton?.setOnClickListener {
            if (address == null) {
                Toast.makeText(this, "Please connect to wallet first", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                resultLauncher.launch(intent)
            }
        }
    }

    suspend fun uploadNFT(result: ActivityResult) {
        // There are no request codes
        val data: Intent? = result.data
        val inputStream = this.contentResolver.openInputStream(data?.data!!)
        imageArray = inputStream!!.readBytes()
        // Upload to ipfs
        runOnUiThread {
            Toast.makeText(this, "Uploading to IPFS", Toast.LENGTH_SHORT).show()
        }
        val imageHash: String = ipfsApi!!.uploadImage(imageArray!!)
        runOnUiThread {
            Toast.makeText(this, "Image uploaded to IPFS", Toast.LENGTH_SHORT).show()
        }

        val metadata: NFTMetaData = NFTMetaData(
            name = "Beautiful picture",
            description = "Pic taken from ethOS",
            mimeType = "image/jpeg",
            version = "zora-20210604",
            image = imageHash
        )
        var gson = Gson()
        val metadataString: String = gson.toJson(metadata)


        val metadataHash: String = ipfsApi!!.uploadString(metadataString)
        val tokenURI = "ipfs://$metadataHash"

        val contract: Media = Media.load(
            "0x7C2668BD0D3c050703CEcC956C11Bd520c26f7d4",
            Web3j.build(EthHttpService("http://localhost:8545")),
            Credentials.create("0x4f3edf983ac636a65a842ce7c78d9aa706d3b113bce9c46f30d7d21715b23b1d"),
            BigInteger.ZERO,
            BigInteger.valueOf(43516)
        )

        GlobalScope.launch {
            runCatching {
                if (imageArray != null) {
                    walletConnectKit?.performTransaction(
                        "0x7C2668BD0D3c050703CEcC956C11Bd520c26f7d4",
                        "0",
                        gasLimit = "150000",
                        data = contract.mint(
                            Media.MediaData(
                                "ipfs://$imageHash",
                                tokenURI,
                                hashMessage(metadataString),
                                hashBytes(imageArray!!)
                            ), Media.BidShares(
                                Media.D256(
                                    BigInteger.valueOf(0)
                                ), Media.D256(
                                    BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(10))
                                ),
                                Media.D256(
                                    BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(90))
                                )
                            )
                        ).encodeFunctionCall()
                    )
                }
            }
                .onSuccess { println("Mint result: Success!") }
                .onFailure {
                    println("Mint result: Fail!")
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val walletConnectButton = findViewById<WalletConnectButton>(R.id.walletConnectButton)
        walletConnectButton.start(walletConnectKit!!) { connectedAddress ->
            if (!connectedToWallet) {
                println("You are connected with account: $connectedAddress")
                connectedToWallet = true;
                address = connectedAddress
                findViewById<View>(R.id.walletConnectButton).visibility = View.INVISIBLE
                findViewById<View>(R.id.connectWalletText).visibility = View.INVISIBLE

                chooseImageButton?.visibility = View.VISIBLE
            }
        }
    }

    fun ByteArray.toHex(): String =
        joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

    fun hashMessage(message: String): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        val input = message.toByteArray(Charsets.UTF_8)
        val bytes = md.digest(input)
        return bytes
    }

    fun hashBytes(message: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(message)
        return bytes
    }

}


 */