package com.example.alpinebuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.data.SessionManager
import com.example.alpinebuddy.databinding.ActivityMainBinding
import com.example.alpinebuddy.ui.adapters.GorovjeAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var gorovjeAdapter: GorovjeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        if (sessionManager.fetchAuthToken() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchGorovja()
    }

    private fun setupRecyclerView() {
        gorovjeAdapter = GorovjeAdapter(emptyList()) { gorovje ->
            val intent = Intent(this, GoreActivity::class.java).apply {
                putExtra(GoreActivity.EXTRA_GOROVJE_ID, gorovje.gorovjeId)
                putExtra(GoreActivity.EXTRA_GOROVJE_NAZIV, gorovje.naziv)
            }
            startActivity(intent)
        }

        binding.rvGorovja.apply {
            adapter = gorovjeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun fetchGorovja() {
        lifecycleScope.launch {
            try {
                val gorovja = ApiClient.apiService.getGorovja()
                Log.d("MainActivity", "Uspešno pridobljeni podatki: $gorovja")
                runOnUiThread {
                    gorovjeAdapter.updateData(gorovja)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Napaka pri pridobivanju podatkov: ", e)
            }
        }
    }
}
