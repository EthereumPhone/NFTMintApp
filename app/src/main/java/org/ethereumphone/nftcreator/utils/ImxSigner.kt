package org.ethereumphone.nftcreator.utils

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.immutable.sdk.Signer
import com.immutable.sdk.StarkSigner
import com.immutable.sdk.crypto.Crypto
import com.immutable.sdk.crypto.StarkKey
import dev.pinkroom.walletconnectkit.WalletConnectKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import org.web3j.crypto.ECKeyPair
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


class ImxSigner(
    walletConnectKit: WalletConnectKit,
) : Signer {
    private val walletConnectKit = walletConnectKit

    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            completableFuture.complete(walletConnectKit.address)

        }

        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            GlobalScope.launch(Dispatchers.Main) {
                completableFuture.complete("\\x19Ethereum Signed Message:\n" + walletConnectKit.personalSign(message).result.toString())
            }
        }

        return completableFuture
    }
}

class ImxStarkSinger(
    walletConnectKit: WalletConnectKit,
    signer: Signer
): StarkSigner {
    private val walletConnectKit = walletConnectKit
    private val signer = signer
    private var ecKeyPair: ECKeyPair?= null



    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            completableFuture.complete(walletConnectKit.address)
        }
        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            if(ecKeyPair == null) {
                StarkKey.generate(signer = signer).whenComplete{ keyPair, error ->
                    ecKeyPair = keyPair
                }.thenApply { completableFuture.complete(StarkKey.sign(ecKeyPair!!, message)) }
            } else {
                completableFuture.complete(StarkKey.sign(ecKeyPair!!, message))
            }
        }
        return completableFuture
    }

}