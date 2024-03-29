package org.ethereumphone.nftcreator.utils

import android.content.Context
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.ethereumphone.nftcreator.contracts.Abi
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthEstimateGas
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import java.security.Provider
import java.security.Security
import java.util.concurrent.CompletableFuture

class ContractInteraction(
    con: Context,
    private val mainnet: Boolean
) {
    private val GOERLI_CONTRACT_ADDRESS = "0x5B48267F7fDb98416C8382C230f4f4AD7453aBd7"
    private val MAINNET_CONTRACT_ADDRESS = "0x7D4960bFcB377307e544ae191CBaF9Dad054552F"
    private val GOERLI_RPC = "https://eth-goerli.g.alchemy.com/v2/ia67i9WXD4d3MV5DSLVOdA45UJCGoJrL"
    private val MAINNET_RPC = "https://cloudflare-eth.com"

    private fun setupBouncyCastle() {
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider::class.java.equals(BouncyCastleProvider::class.java)) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

    init {
        setupBouncyCastle()
    }


    private var web3j: Web3j? = null
    private var nftMintContract: Abi? = null
    private var selectedContract: String = ""
    private var gasEstimate: EthEstimateGas? = null
    val credentials: Credentials = Credentials.create(Keys.createEcKeyPair())
    private val context = con
    private var selectedRPC = ""

    fun load() {
        selectedContract = if (mainnet) MAINNET_CONTRACT_ADDRESS else GOERLI_CONTRACT_ADDRESS
        try {
            if (!mainnet) {
                web3j = Web3j.build(HttpService(GOERLI_RPC))
                selectedRPC = GOERLI_RPC
            } else {
                web3j = Web3j.build(HttpService())
                web3j?.ethChainId()?.sendAsync()?.get()
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
        val onPhoneAddr = WalletSDK(context).getAddress()

        val transaction = Transaction.createFunctionCallTransaction(
            onPhoneAddr,
            web3j!!.ethGetTransactionCount(
                onPhoneAddr, DefaultBlockParameterName.LATEST
            ).sendAsync().get().transactionCount,
            web3j?.ethGasPrice()?.sendAsync()?.get()?.gasPrice?.multiply(BigInteger("2")),
            BigInteger("300000"),
            selectedContract,
            data
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
    ): CompletableFuture<String> {
        val data = nftMintContract?.mintImage(address, tokenURI)?.encodeFunctionCall()
        val wallet =
            WalletSDK(context, web3RPC = selectedRPC)
        if (data != null) {
            gasEstimation(address, tokenURI)
            println(gasEstimate)
        }

        return wallet.sendTransaction(
            to = selectedContract,
            value = "0",
            data = data!!,
            gasAmount = "240000",
            chainId = if (mainnet) 1 else 5 // Mainnet or Goerli
        )

    }
}
