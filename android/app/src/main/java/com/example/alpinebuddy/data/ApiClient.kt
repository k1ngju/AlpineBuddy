package com.example.alpinebuddy.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

// Vmesnik, ki definira API končne točke
interface ApiService {
    @GET("/gorovja")
    suspend fun getGorovja(): List<GorovjeRead>
}

object ApiClient {

    // Tvoj lokalni IP naslov. Zamenjaj z dejanskim, če ni pravilen.
    // Na Android Emulatorju je 10.0.2.2 standarden naslov za dostop do localhosta računalnika.
    internal const val BASE_URL = "http://10.0.2.2:8000"

    internal val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Ustvarimo Retrofit instanco
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Ustvarimo implementacijo ApiService vmesnika, do katere lahko dostopamo od drugod
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
