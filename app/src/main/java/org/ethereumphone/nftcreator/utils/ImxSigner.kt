package org.ethereumphone.nftcreator.utils

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.immutable.sdk.Signer
import com.immutable.sdk.StarkSigner
import dev.pinkroom.walletconnectkit.WalletConnectKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


class ImxSigner(
    walletViewModel: ConnectWalletViewModel? = null,
    walletConnectKit: WalletConnectKit,
    context: Context
) : Signer, StarkSigner {
    private val wallet = walletViewModel
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