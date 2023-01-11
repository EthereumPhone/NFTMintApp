package org.ethereumphone.nftcreator.utils


import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import com.immutable.sdk.Signer
import com.immutable.sdk.StandardStarkSigner
import com.immutable.sdk.StarkSigner
import com.immutable.sdk.crypto.StarkKey
import dev.pinkroom.walletconnectkit.WalletConnectKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.RawTransaction
import java.util.concurrent.CompletableFuture


class ImxSigner(
    walletConnectKit: WalletConnectKit? = null,
    val context: Context
) : Signer {

    val wallet = WalletSDK(context)

    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        CompletableFuture.runAsync {
            completableFuture.complete(wallet.getAddress())
        }
        return completableFuture
    }

    override fun sendTransaction(rawTransaction: RawTransaction): CompletableFuture<String> {
        TODO("Not yet implemented")
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        return wallet.signMessage(message)
    }
}

fun calcByteLength(length: Int, byteSize: Int = 8): Int {
    val remainder = length % byteSize
    return if (remainder != 0) ((length - remainder) / byteSize) * byteSize + byteSize else length
}

fun hexToBin(ch: Char): Int = when (ch) {
    in '0'..'9' -> ch - '0'
    in 'A'..'F' -> ch - 'A' + 10
    in 'a'..'f' -> ch - 'a' + 10
    else -> throw(IllegalArgumentException("'$ch' is not a valid hex character"))
}


fun String.hexToByteArray(): ByteArray {
    // An hex string must always have length multiple of 2
    if (length % 2 != 0) {
        throw IllegalArgumentException("hex-string must have an even number of digits (nibbles)")
    }

    // Remove the 0x prefix if it is set
    val cleanInput = if (startsWith("0x")) substring(2) else this

    return ByteArray(cleanInput.length / 2).apply {
        var i = 0
        while (i < cleanInput.length) {
            this[i / 2] = (
                    (hexToBin(cleanInput[i]) shl 4) +
                            hexToBin(cleanInput[i + 1])
                    ).toByte()
            i += 2
        }
    }
}


class ImxStarkSinger(
    signer: Signer,
    sharedPreferences: SharedPreferences
): StarkSigner {
    var starkPrivateKey = StarkKey.generateStarkPrivateKey()
    lateinit var starkSigner : StandardStarkSigner

    init {
        if (sharedPreferences.contains("starkPrivateKey")) {
            val starkPrivateKey = sharedPreferences.getString("starkPrivateKey", null)
            if (starkPrivateKey != null) {
                this.starkPrivateKey = starkPrivateKey
            }
        } else {
            sharedPreferences.edit().putString("starkPrivateKey", starkPrivateKey).apply()
        }
        starkSigner = StandardStarkSigner(starkPrivateKey)
    }


    override fun getAddress(): CompletableFuture<String> {
        return starkSigner.getAddress()
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        return starkSigner.signMessage(message)
    }
}