package org.ethereumphone.nftcreator.utils

import com.immutable.sdk.Signer
import com.immutable.sdk.StarkSigner
import org.ethereumphone.nftcreator.walletconnect.ConnectWalletViewModel
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


class ImxSigner(
    walletViewModel: ConnectWalletViewModel
) : Signer, StarkSigner {
    private val wallet = walletViewModel
    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            completableFuture.complete(wallet.userWallet.value)
        }

        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()
        wallet.signMessage(
            message = message
        ) {
            Executors.newCachedThreadPool().submit {
                completableFuture.complete(it.result.toString())
            }
        }
        return completableFuture
    }

}