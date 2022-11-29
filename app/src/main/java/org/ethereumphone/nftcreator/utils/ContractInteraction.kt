package org.ethereumphone.nftcreator.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.ethereumphone.nftcreator.contracts.Abi
import org.ethereumphone.walletsdk.WalletSDK
import org.web3j.abi.FunctionEncoder
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthEstimateGas
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

private const val GOERLI_CONTRACT_ADDRESS = "0x5B48267F7fDb98416C8382C230f4f4AD7453aBd7"

private var web3j: Web3j? = null
private var nftMintContract: Abi? = null
private var selectedContract: String = ""
private var gasEstimate: EthEstimateGas? = null
val credentials: Credentials = Credentials.create(Keys.createEcKeyPair())

fun load(
    contract: String,
) {
    selectedContract = contract
    web3j = try {
        Web3j.build(HttpService())
    } catch(e: Exception) {
        Web3j.build(HttpService("https://cloudflare-eth.com"))
    }
    nftMintContract = Abi.load(
        selectedContract,
        web3j,
        credentials,
        DefaultGasProvider.GAS_PRICE,
        DefaultGasProvider.GAS_LIMIT
    )

}


fun gasEstimation(
    address: String,
    tokenURI: String
) {
    val data = nftMintContract?.mintImage(address, tokenURI)?.encodeFunctionCall()

    val transaction = Transaction.createFunctionCallTransaction(
        credentials.address,
        BigInteger.ONE,
        web3j?.ethGasPrice()?.sendAsync()?.get()?.gasPrice,
        DefaultGasProvider.GAS_LIMIT,
        selectedContract,
        BigInteger.valueOf(10000000000), data)

    gasEstimate = web3j?.ethEstimateGas(transaction)?.sendAsync()?.get()
}

fun gasEstimation(
    data: String
) {
    val transaction = Transaction.createFunctionCallTransaction(
        credentials.address,
        BigInteger.ONE,
        web3j?.ethGasPrice()?.sendAsync()?.get()?.gasPrice,
        DefaultGasProvider.GAS_LIMIT,
        selectedContract,
        BigInteger.valueOf(10000000000), data)

    gasEstimate = web3j?.ethEstimateGas(transaction)?.sendAsync()?.get()
}

@Composable
fun mintImage(
    address: String,
    tokenURI: String
) {
    val data = nftMintContract?.mintImage(address, tokenURI)?.encodeFunctionCall()
    val wallet = WalletSDK(LocalContext.current)
    if (data != null) {
        gasEstimation(data)
    }

    if (data != null) {
        gasEstimate?.let {
            wallet.sendTransaction(
                to = selectedContract,
                value = "0x0",
                data = data,
                gasAmount = it.result
            )
        }
    }
}
