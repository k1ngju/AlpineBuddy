package com.example.alpinebuddy.data

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class AuthService {

    private val client = ApiClient.okHttpClient
    private val gson = Gson()

    fun login(email: String, geslo: String, callback: (TokenResponse?, String?) -> Unit) {
        // FastAPI OAuth2PasswordRequestForm zahteva form-data s ključema username in password
        val formBody = FormBody.Builder()
            .add("username", email)
            .add("password", geslo)
            .build()

        val request = Request.Builder()
            .url("${ApiClient.BASE_URL}/auth/token")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e.message ?: "Neznana napaka pri povezavi")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val tokenResponse = gson.fromJson(body, TokenResponse::class.java)
                    callback(tokenResponse, null)
                } else {
                    callback(null, "Prijava ni uspela. Preveri podatke.")
                }
            }
        })
    }
}
