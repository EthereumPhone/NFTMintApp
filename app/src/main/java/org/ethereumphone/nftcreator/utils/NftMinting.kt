package org.ethereumphone.nftcreator.utils

import com.immutable.sdk.Signer
import java.util.concurrent.CompletableFuture


class ImxSigner : Signer {
    override fun getAddress(): CompletableFuture<String> {
        TODO("Not yet implemented")
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        TODO("Not yet implemented")
    }

}