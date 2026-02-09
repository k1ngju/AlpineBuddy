package com.example.alpinebuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alpinebuddy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nastavimo ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tukaj bomo kasneje dodali preverjanje, če je uporabnik že prijavljen
        // Če ni, ga preusmerimo na LoginActivity
    }
}
