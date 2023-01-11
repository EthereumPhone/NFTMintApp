package org.ethereumphone.nftcreator.moduls

import java.io.Serializable

data class Network(
    val chainId: Int,
    val chainRPC: String,
    val chainName: String,
    val chainExplorer: String,
    val contractAddress: String
) : Serializable

