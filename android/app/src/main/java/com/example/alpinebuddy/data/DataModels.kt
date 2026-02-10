package com.example.alpinebuddy.data

import com.google.gson.annotations.SerializedName

// Odgovor, ki ga dobimo iz /auth/token
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

// Podatki o uporabniku glede na schemas.py -> UporabnikRead
data class UporabnikRead(
    @SerializedName("uporabnik_id") val uporabnikId: Int,
    val ime: String,
    val email: String
)

// Podatki za registracijo uporabnika
data class UserRegistration(
    val ime: String,
    val email: String,
    val geslo: String
)

// Podatki o gorovju glede na schemas.py -> GorovjeRead
data class GorovjeRead(
    @SerializedName("gorovje_id") val gorovjeId: Int,
    val naziv: String?,
    val opis: String?,
    @SerializedName("slika_url") val slikaUrl: String?
)

// Podatki o gori glede na schemas.py -> GoraRead
data class GoraRead(
    @SerializedName("gora_id") val goraId: Int,
    @SerializedName("ime") val naziv: String,
    @SerializedName("slika_url") val slikaUrl: String?,
    @SerializedName("gorovje_id") val gorovjeId: Int
)

// Podatki o smeri glede na schemas.py -> SmerRead
data class SmerRead(
    @SerializedName("smer_id") val smerId: Int,
    @SerializedName("gora_id") val goraId: Int,
    @SerializedName("tezavnost_id") val tezavnostId: Int?,
    @SerializedName("stil_id") val stilId: Int?,
    val ime: String?,
    @SerializedName("dolzina_m") val dolzina: Int?,
    val opis: String?,
    @SerializedName("skica_url_1") val skicaUrl1: String?,
    @SerializedName("skica_url_2") val skicaUrl2: String?
)

// Podatki o težavnosti smeri glede na schemas.py -> TezavnostRead
data class TezavnostRead(
    @SerializedName("tezavnost_id") val tezavnostId: Int,
    val oznaka: String,
    val sistem: String?
)

// Podatki o stilu smeri glede na schemas.py -> StilSmeriRead
data class StilSmeriRead(
    @SerializedName("stil_id") val stilId: Int,
    val naziv: String,
    val opis: String?
)
