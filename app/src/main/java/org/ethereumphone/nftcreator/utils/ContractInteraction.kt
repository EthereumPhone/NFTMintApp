package org.ethereumphone.nftcreator.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.ethereumphone.nftcreator.contracts.Abi
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthEstimateGas
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

class ContractInteraction(
    con: Context,
    private val mainnet: Boolean
) {
    private val GOERLI_CONTRACT_ADDRESS = "0x5B48267F7fDb98416C8382C230f4f4AD7453aBd7"
    private val GOERLI_RPC = "https://eth-goerli.g.alchemy.com/v2/XXXXX"
    private val MAINNET_RPC = "https://cloudflare-eth.com"

    private var web3j: Web3j? = null
    private var nftMintContract: Abi? = null
    private var selectedContract: String = ""
    private var gasEstimate: EthEstimateGas? = null
    val credentials: Credentials = Credentials.create(Keys.createEcKeyPair())
    private val context = con
    private var selectedRPC = ""

    fun load() {
        selectedContract = if (mainnet) "" else GOERLI_CONTRACT_ADDRESS
        try {
            if (!mainnet) {
                web3j = Web3j.build(HttpService(GOERLI_RPC))
                selectedRPC = GOERLI_RPC
            } else {
                web3j = Web3j.build(HttpService())
                selectedRPC = "http://127.0.0.1:8545"
            }
        } catch (e: Exception) {
            web3j = Web3j.build(HttpService(if (mainnet) MAINNET_RPC else GOERLI_RPC))
            selectedRPC = if (mainnet) MAINNET_RPC else GOERLI_RPC
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
            BigInteger.valueOf(10000000000), data
        )

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
            BigInteger.valueOf(10000000000), data
        )

        gasEstimate = web3j?.ethEstimateGas(transaction)?.sendAsync()?.get()
    }


    fun mintImage(
        address: String,
        tokenURI: String
    ) {
        val data = nftMintContract?.mintImage(address, tokenURI)?.encodeFunctionCall()
        val wallet =
            WalletSDK(context, web3RPC = selectedRPC)
        if (data != null) {
            gasEstimation(data)
        }

        if (data != null) {
            gasEstimate?.let {
                wallet.sendTransaction(
                    to = selectedContract,
                    value = "0x0",
                    data = data,
                    gasAmount = it.result,
                    chainId = if (mainnet) 1 else 5 // Mainnet or Goerli
                )
            }
        }
    }
}
