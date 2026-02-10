package com.example.alpinebuddy

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.data.UserRegistration
import com.example.alpinebuddy.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Registracija"

        binding.btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    private fun performRegistration() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!validateInput(name, email, password)) {
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                val userData = UserRegistration(ime = name, email = email, geslo = password)
                val response = ApiClient.apiService.registerUser(userData)

                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registracija uspešna! Sedaj se lahko prijavite.", Toast.LENGTH_LONG).show()
                    finish() // Vrne uporabnika na prijavni zaslon
                } else {
                    val errorBody = response.errorBody()?.string()
                    // Predpostavljamo, da backend vrne JSON z 'detail' poljem
                    val errorMessage = if (errorBody != null && errorBody.contains("Email already registered")) {
                        "Uporabnik s tem emailom že obstaja."
                    } else {
                        "Napaka pri registraciji: ${response.code()}"
                    }
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Napaka: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            binding.etName.error = "Ime ne sme biti prazno."
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Vnesite veljaven email naslov."
            return false
        }
        if (password.length < 8) {
            binding.etPassword.error = "Geslo mora imeti vsaj 8 znakov."
            return false
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
