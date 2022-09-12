package org.ethereumphone.nftcreator.utils

import com.immutable.sdk.api.model.MintRequest
import com.immutable.sdk.api.model.MintTokenDataV2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.CompletableFuture

fun mintToken(address: String, mintData: MintTokenDataV2, blueprint: NFTMetadata): CompletableFuture<String> {
    val completableFuture = CompletableFuture<String>();
    val url = "https://us-central1-imx-minting-ethos.cloudfunctions.net/mint?wallet=${URLEncoder.encode(address, "utf-8")}" +
            "&tokenID=${URLEncoder.encode(mintData.id, "utf-8")}" +
            "&blueprint=${URLEncoder.encode(blueprint.name,"utf-8")}" +
            "&ipfsHash=${URLEncoder.encode("https://nftmintapp.infura-ipfs.io/ipfs/"+blueprint.image, "utf-8")}"
    GlobalScope.launch(Dispatchers.IO) {
        val response = async { URL(url).readText() }.await()
        completableFuture.complete(response)
    }
    return completableFuture
}