package com.example.alpinebuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.data.SessionManager
import com.example.alpinebuddy.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        // Preveri, če je uporabnik prijavljen (če ima žeton)
        if (sessionManager.fetchAuthToken() == null) {
            // Če nima žetona, pojdi na LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWelcome.text = getString(R.string.welcome_message)

        // Kličemo API za pridobitev gorovij
        fetchGorovja()
    }

    private fun fetchGorovja() {
        lifecycleScope.launch {
            try {
                val gorovja = ApiClient.apiService.getGorovja()
                // Zaenkrat samo izpišemo v Logcat
                Log.d("MainActivity", "Uspešno pridobljeni podatki: $gorovja")
                // TODO: Prikaz podatkov v RecyclerView
            } catch (e: Exception) {
                // V primeru napake izpišemo error
                Log.e("MainActivity", "Napaka pri pridobivanju podatkov: ", e)
            }
        }
    }
}
