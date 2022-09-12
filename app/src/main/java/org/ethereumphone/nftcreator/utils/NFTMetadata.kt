package org.ethereumphone.nftcreator.utils

import kotlinx.serialization.Serializable

@Serializable
data class NFTMetadata(
    var name: String?,
    var image: String?,
    var description: String?,
)