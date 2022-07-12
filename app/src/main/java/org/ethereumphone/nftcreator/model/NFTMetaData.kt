package org.ethereumphone.nftcreator.model

import kotlinx.serialization.Serializable

@Serializable
data class NFTMetaData(
    val description: String,
    val mimeType: String,
    val name: String,
    val version: String,
    val image: String
    )