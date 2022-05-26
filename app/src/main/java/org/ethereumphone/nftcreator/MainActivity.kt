package org.ethereumphone.nftcreator

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import dev.pinkroom.walletconnectkit.WalletConnectButton
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.math.pow


import kotlinx.serialization.*
import kotlinx.serialization.json.*

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
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val inputStream = this.contentResolver.openInputStream(data?.data!!)
                imageArray = inputStream!!.readBytes()
                // Upload to ipfs
                Toast.makeText(this, "Uploading to IPFS", Toast.LENGTH_SHORT).show()
                val imageHash: String = ipfsApi!!.uploadImage(imageArray!!)
                Toast.makeText(this, "Image uploaded to IPFS", Toast.LENGTH_SHORT).show()

                val metadata: NFTMetaData = NFTMetaData(name = "Beautiful picture", description = "Pic taken from ethOS", mimeType = "image/jpeg", version = "zora-20210604", image = imageHash)
                var gson = Gson()
                val metadataString: String = gson.toJson(metadata)


                val metadataHash: String = ipfsApi!!.uploadString(metadataString)
                val tokenURI = "ipfs://$metadataHash"

                val contract:Media = Media.load("0x7C2668BD0D3c050703CEcC956C11Bd520c26f7d4", Web3j.build(EthHttpService("http://localhost:8545")),Credentials.create("0x4f3edf983ac636a65a842ce7c78d9aa706d3b113bce9c46f30d7d21715b23b1d"), BigInteger.ZERO, BigInteger.valueOf(43516))

                GlobalScope.launch {
                    runCatching {
                        if (imageArray != null) {
                            walletConnectKit?.performTransaction("0x7C2668BD0D3c050703CEcC956C11Bd520c26f7d4", "0", gasLimit = "150000", data = contract.mint(
                                Media.MediaData("ipfs://$imageHash", tokenURI, hashMessage(metadataString), hashBytes(imageArray!!)), Media.BidShares(
                                    Media.D256(
                                        BigInteger.valueOf(0)), Media.D256(
                                        BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(10))),
                                    Media.D256(
                                        BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(90))))).encodeFunctionCall())
                        }
                    }
                        .onSuccess { println("Mint result: Success!") }
                        .onFailure {
                            println("Mint result: Fail!")
                        }
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

                Toast.makeText(this, "Your address: $address", Toast.LENGTH_LONG).show()

                val mintButton = findViewById<Button>(R.id.mint)
                mintButton.visibility = View.VISIBLE
                chooseImageButton?.visibility = View.VISIBLE
            }
        }
    }

    fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

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
    /**
    fun initiateUIAfterConnect(address: String) {
        findViewById<View>(R.id.walletConnectButton).visibility = View.INVISIBLE
        findViewById<View>(R.id.connectWalletText).visibility = View.INVISIBLE

        Toast.makeText(this, "Your address: $address", Toast.LENGTH_LONG).show()

        val mintButton = findViewById<Button>(R.id.mint)
        mintButton.visibility = View.VISIBLE
        chooseImageButton?.visibility = View.VISIBLE
        val contract:Media = Media.load("0x7C2668BD0D3c050703CEcC956C11Bd520c26f7d4", Web3j.build(HttpService("http://localhost:8545")),Credentials.create("0x4f3edf983ac636a65a842ce7c78d9aa706d3b113bce9c46f30d7d21715b23b1d"), BigInteger.ZERO, BigInteger.valueOf(43516))
        mintButton.setOnClickListener {
            GlobalScope.launch {
                runCatching {
                    if (imageArray != null) {
                        walletConnectKit?.performTransaction("0x7C2668BD0D3c050703CEcC956C11Bd520c26f7d4", "0", gasLimit = "150000", data = contract.mint(
                        Media.MediaData("https://ipfs.io/ipfs/QmbnNqx5wteobq29E4EeSoTpacDsPFNvnG93nFvKvmK9X5","https://ipfs.io/ipfs/QmdTHB6W9PWpJwD7jdTyKRmdXhF8y4g6vuj6Lnz5M2P29r", hashMessage("{\n" +
                                "    \"name\": \"\",\n" +
                                "    \"description\": \"\",\n" +
                                "    \"image\": \"https://ipfs.io/ipfs/QmdTHB6W9PWpJwD7jdTyKRmdXhF8y4g6vuj6Lnz5M2P29r\",\n" +
                                "}"), hashBytes(imageArray!!)), Media.BidShares(
                            Media.D256(
                                BigInteger.valueOf(0)), Media.D256(
                                BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(10))),
                                Media.D256(
                                    BigInteger.TEN.pow(18).multiply(BigInteger.valueOf(90))))).encodeFunctionCall())
                    }
                }
                    .onSuccess { println("Mint result: Success!") }
                    .onFailure {
                        println("Mint result: Fail!")
                    }
            }
        }


    }
    */


}