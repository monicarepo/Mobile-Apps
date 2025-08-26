package com.ms.countryapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Demonyms(
    val eng: Eng? = null
)

@Serializable
data class Eng(
    val f: String? = null,
    val m: String? = null
)

@Serializable
data class Idd(
    val root: String? = null,
    val suffixes: List<String>? = null
)