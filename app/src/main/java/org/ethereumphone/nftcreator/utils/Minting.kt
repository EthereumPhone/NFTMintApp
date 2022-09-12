package org.ethereumphone.nftcreator.utils

import android.util.Log
import com.google.gson.Gson
import com.immutable.sdk.ImmutableXCore
import com.immutable.sdk.api.MintsApi
import com.immutable.sdk.api.UsersApi
import com.immutable.sdk.api.model.*
import com.immutable.sdk.crypto.Crypto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.openapitools.client.infrastructure.ClientException
import java.net.HttpURLConnection
import java.util.concurrent.CompletableFuture

private const val CONTRACT_ADDRESS = "0x5062fD4a4F56A6241047CF476E1484b0e46d18EE" //ethOS minting contract


/**
 * This function allows to mint a ERC-Token via ImmutableX
 */
@Suppress("LongParameterList")
internal fun mintingWorkFlow(
    signer: ImxSigner,
    starkSinger: ImxStarkSinger,
    usersApi: UsersApi = UsersApi(),
    mintsApi: MintsApi = MintsApi(),
    ipfsHash: String,
    blueprint: String?
): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    var userWallet = ""

    var bp = NFTMetadata(
        name = "test",
        image = ipfsHash,
        description = "this is a test mint",
    )

    var BpString = Json.encodeToString(bp)

    var royalties = MintFee(
        percentage = 2.0,
        recipient = userWallet
    )

    var tokenData = MintTokenDataV2(
        id = "0", // wont work if same id
        blueprint = BpString,
        royalties = listOf(royalties)
    )

    var user = MintUser(
        tokens = listOf(tokenData),
        user = userWallet
    )

    var request = MintRequest(
        authSignature = "",
        contractAddress = CONTRACT_ADDRESS,
        users = listOf(user),
        royalties = listOf(royalties)
    )

    signer.getAddress().thenApply { address ->
        userWallet = address

        // royalties
        royalties = MintFee(
            percentage = 2.0,
            recipient = userWallet
        )
        // token data
        tokenData = MintTokenDataV2(
            // TODO: change how id is generated (Markus was warned :D)
            id = (0..1000000).random().toString(),

            blueprint = BpString,
            royalties = listOf(royalties)
        )
        // mint user
        user = MintUser(
            tokens = listOf(tokenData),
            user = userWallet
        )
        // mint request
        request = MintRequest(
            authSignature = "",
            contractAddress = CONTRACT_ADDRESS,
            users = listOf(user),
            royalties = listOf(royalties)
        )
        isWalletRegistered(userWallet, usersApi) }
        .thenCompose { isConnected -> connectWallet(isConnected, signer, starkSinger) }
        .thenApply {
            mintToken(address = userWallet, mintData = tokenData, blueprint = BpString)
            .whenComplete {
                response, error ->
                if(error != null) future.completeExceptionally(error)
                else future.complete(response)
            }
        }
    return future
}

@Suppress("TooGenericExceptionCaught", "InstanceOfCheckForException")
private fun isWalletRegistered(address: String, userApi: UsersApi): Boolean {
    return try {
        userApi.getUsers(address).accounts.isNotEmpty()
    } catch (e: Exception) {
        if (e is ClientException && e.statusCode == HttpURLConnection.HTTP_NOT_FOUND) false
        else throw e
    }
}

private fun connectWallet(
    isConnected: Boolean,
    signer: ImxSigner,
    starkSinger: ImxStarkSinger
): CompletableFuture<Unit> {
    return if(!isConnected) ImmutableXCore.registerOffChain(signer,starkSinger)
    else CompletableFuture.completedFuture(Unit)
}




