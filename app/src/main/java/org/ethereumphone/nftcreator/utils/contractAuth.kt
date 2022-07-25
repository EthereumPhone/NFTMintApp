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

fun getSignature(mint: MintRequest, address: String, mintData: MintTokenDataV2): CompletableFuture<String> {
    val completableFuture = CompletableFuture<String>();
    val url = "https://us-central1-imx-minting-ethos.cloudfunctions.net/getSignature?address=${URLEncoder.encode(address, "utf-8")}&id=${URLEncoder.encode(mintData.id, "utf-8")}&blueprint=${URLEncoder.encode(mintData.blueprint, "utf-8")}&contractAddress=${URLEncoder.encode(mint.contractAddress, "utf-8")}"
    GlobalScope.launch(Dispatchers.IO) {
        val response = async { URL(url).readText() }.await()
        completableFuture.complete(response)
    }
    return completableFuture
}