package com.example.alpinebuddy.data

import com.google.gson.annotations.SerializedName

// Odgovor, ki ga dobimo iz /auth/token
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

// Podatki o uporabniku
data class Uporabnik(
    val id: Int,
    val ime: String,
    val email: String
)
