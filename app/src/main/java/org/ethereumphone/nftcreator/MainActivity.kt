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

class MainActivity : AppCompatActivity() {

    private var connectedToWallet: Boolean = false
    private var walletConnectButton: WalletConnectButton? = null
    private var walletConnectKit: WalletConnectKit? = null
    var chooseImageButton: Button? = null
    var imageArray: ByteArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                var inputStream = this.contentResolver.openInputStream(data?.data!!)
                imageArray = inputStream!!.readBytes()
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val walletConnectButton = findViewById<WalletConnectButton>(R.id.walletConnectButton)
        walletConnectButton.start(walletConnectKit!!) { address ->
            if (!connectedToWallet) {
                println("You are connected with account: $address")
                connectedToWallet = true;
                initiateUIAfterConnect(address);
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
                                "    \"name\": \"Beautiful picture\",\n" +
                                "    \"description\": \"Pic taken from ethOSs\",\n" +
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


}