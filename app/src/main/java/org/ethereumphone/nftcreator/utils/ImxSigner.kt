package org.ethereumphone.nftcreator.utils


import com.immutable.sdk.Signer
import com.immutable.sdk.StarkSigner
import com.immutable.sdk.crypto.StarkKey
import com.immutable.sdk.extensions.getStarkPublicKey
import dev.pinkroom.walletconnectkit.WalletConnectKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.web3j.crypto.ECKeyPair
import java.util.concurrent.CompletableFuture


class ImxSigner(
    walletConnectKit: WalletConnectKit,
) : Signer {
    private val walletConnectKit = walletConnectKit

    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        CompletableFuture.runAsync {
            completableFuture.complete(walletConnectKit.address)
        }
        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        CompletableFuture.runAsync {
            GlobalScope.launch(Dispatchers.Main) {
                completableFuture.complete(walletConnectKit.personalSign(message).result.toString())
            }
        }
        return completableFuture
    }
}

class ImxStarkSinger(signer: Signer,
): StarkSigner {
    private var ecKeyPair : ECKeyPair? = null
    private val signer = signer


    override fun getAddress(): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()

        if(ecKeyPair==null){
            CompletableFuture.runAsync {
                StarkKey.generate(signer).whenComplete { keyPair, error ->
                    ecKeyPair = keyPair
                    completableFuture.complete(ecKeyPair!!.getStarkPublicKey())
                }
            }
        } else {
            CompletableFuture.runAsync {
                completableFuture.complete(ecKeyPair!!.getStarkPublicKey())
            }
        }
        return completableFuture
    }

    override fun signMessage(message: String): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()
        if (ecKeyPair == null){
            CompletableFuture.runAsync {
                StarkKey.generate(signer).whenComplete { keyPair, error ->
                    ecKeyPair = keyPair
                    completableFuture.complete(StarkKey.sign(ecKeyPair!!, message))
                }
            }
        } else {
            CompletableFuture.runAsync {
                completableFuture.complete(StarkKey.sign(ecKeyPair!!, message))
            }
        }

        return completableFuture
    }
}