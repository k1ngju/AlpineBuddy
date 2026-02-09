package com.example.alpinebuddy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alpinebuddy.data.AuthService
import com.example.alpinebuddy.data.SessionManager
import com.example.alpinebuddy.data.TokenResponse
import com.example.alpinebuddy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AuthService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authService = AuthService()
        sessionManager = SessionManager(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vnesi email in geslo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false

            authService.login(email, password) { tokenResponse: TokenResponse?, error: String? ->
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true

                    if (tokenResponse != null) {
                        sessionManager.saveAuthToken(tokenResponse.accessToken)
                        Toast.makeText(this, "Prijava uspešna!", Toast.LENGTH_SHORT).show()
                        
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, error ?: "Napaka pri prijavi", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            Toast.makeText(this, "Registracija še ni implementirana", Toast.LENGTH_SHORT).show()
        }
    }
}
