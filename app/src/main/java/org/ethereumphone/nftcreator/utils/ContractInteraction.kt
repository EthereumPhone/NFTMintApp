package org.ethereumphone.nftcreator.utils

import android.content.Context
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.ethereumphone.nftcreator.contracts.Abi
import org.ethereumphone.nftcreator.moduls.Network
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthEstimateGas
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.lang.Long
import java.math.BigInteger
import java.security.Provider
import java.security.Security
import java.util.concurrent.CompletableFuture

class ContractInteraction(
    con: Context,
    private val selectedNetwork: Network
) {

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
        selectedContract = selectedNetwork.contractAddress
        try {
            if (selectedNetwork.chainId != 1) {
                web3j = Web3j.build(HttpService(selectedNetwork.chainRPC))
                selectedRPC = selectedNetwork.chainRPC
            } else {
                web3j = Web3j.build(HttpService())
                web3j?.ethChainId()?.sendAsync()?.get()
                selectedRPC = "http://127.0.0.1:8545"
            }
        } catch (e: Exception) {
            web3j = Web3j.build(HttpService(selectedNetwork.chainRPC))
            selectedRPC = selectedNetwork.chainRPC
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

        val gas = web3j?.ethEstimateGas(
            org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction(
                wallet.getAddress(),
                null,
                null,
                null,
                selectedContract,
                BigInteger.ZERO,
                data
            )
        )?.send()
        println("Gas: ${gas?.amountUsed}")

        return wallet.sendTransaction(
            to = selectedContract,
            value = "0",
            data = data!!,
            gasAmount = gas?.amountUsed.toString(),
            chainId = selectedNetwork.chainId
        )

    }
}
