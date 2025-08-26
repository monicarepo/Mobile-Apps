package com.ms.countryapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val common: String? = null,
    val nativeName: NativeName? = null,
    val official: String? = null
)

@Serializable
data class NativeName(
    val nor: Nor? = null
)

@Serializable
data class Nor(
    val common: String? = null,
    val official: String? = null
)