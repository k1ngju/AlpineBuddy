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
