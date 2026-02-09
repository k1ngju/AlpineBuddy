package com.example.alpinebuddy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alpinebuddy.data.SessionManager
import com.example.alpinebuddy.databinding.ActivityMainBinding

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
            finish() // Zapri MainActivity
            return
        }

        // Če je prijavljen, nastavi ViewBinding za glavno okno
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvWelcome.text = "Pozdravljen v AlpineBuddy!"
    }
}
