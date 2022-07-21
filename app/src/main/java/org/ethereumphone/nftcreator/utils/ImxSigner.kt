package org.ethereumphone.nftcreator.utils

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.immutable.sdk.Signer
import com.immutable.sdk.StarkSigner
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
    walletViewModel: ConnectWalletViewModel? = null,
    walletConnectKit: WalletConnectKit,
    context: Context
) : Signer {
    //private val wallet = walletViewModel
    private val context = context
    private val walletConnectKit = walletConnectKit

    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            //completableFuture.complete(wallet?.userWallet?.value)
            completableFuture.complete(walletConnectKit.address)

        }

        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()
        /*
        wallet?.signMessage(
            message = message,
            context = context
        ) {
            Executors.newCachedThreadPool().submit {
                completableFuture.complete(it.result.toString())
            }
        }
        */
        Executors.newCachedThreadPool().submit {
            GlobalScope.launch(Dispatchers.Main) {
                completableFuture.complete(walletConnectKit.personalSign(message).result.toString())
            }
        }

        return completableFuture
    }
}

class ImxStarkSinger(
    walletConnectKit: WalletConnectKit,
    context: Context
): StarkSigner {
    private val walletConnectKit = walletConnectKit
    private val context = context

    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            //completableFuture.complete(wallet?.userWallet?.value)
            completableFuture.complete(walletConnectKit.address)

        }
        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            StarkKey.generate(ImxSigner(walletConnectKit = walletConnectKit, context = context)).whenComplete { keyPair, error ->
                val signOutput = StarkKey.sign(keyPair, message)

                completableFuture.complete(signOutput)
            }


        }
        return completableFuture
    }

}