package com.example.alpinebuddy.data

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiClient {

    // Tvoj lokalni IP naslov. Zamenjaj z dejanskim, če ni pravilen.
    // Na Android Emulatorju je 10.0.2.2 standarden naslov za dostop do localhosta računalnika.
    const val BASE_URL = "http://10.0.2.2:8000"

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}
