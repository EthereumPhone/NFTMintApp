package org.ethereumphone.nftcreator.moduls

data class TokenData (
    val name: String,
    val description: String,
    val image: String,
    val attributes: List<MinterAttribute>
)

data class MinterAttribute(
    val trait_type: String,
    val value: String
)
