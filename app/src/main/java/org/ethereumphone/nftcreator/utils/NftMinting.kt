package org.ethereumphone.nftcreator.utils

import com.immutable.sdk.Signer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


class ImxSigner : Signer {
    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            Thread.sleep(500)
            //TODO: Return valid connected address
            completableFuture.complete("Hello")
        }

        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        Executors.newCachedThreadPool().submit {
            Thread.sleep(500)
            //TODO: Sign passed message and return result
            completableFuture.complete("Hello")
        }

        return completableFuture
    }

}