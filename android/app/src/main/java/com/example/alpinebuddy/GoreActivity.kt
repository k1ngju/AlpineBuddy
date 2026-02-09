package com.example.alpinebuddy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.databinding.ActivityGoreBinding
import com.example.alpinebuddy.ui.adapters.GoraAdapter
import kotlinx.coroutines.launch

class GoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoreBinding
    private lateinit var goraAdapter: GoraAdapter

    companion object {
        const val EXTRA_GOROVJE_ID = "gorovje_id"
        const val EXTRA_GOROVJE_NAZIV = "gorovje_naziv"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gorovjeId = intent.getIntExtra(EXTRA_GOROVJE_ID, -1)
        val gorovjeNaziv = intent.getStringExtra(EXTRA_GOROVJE_NAZIV)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = gorovjeNaziv ?: "Gore"

        setupRecyclerView()

        if (gorovjeId != -1) {
            fetchAndFilterGore(gorovjeId)
        } else {
            Log.e("GoreActivity", "Napačen ali manjkajoč ID gorovja.")
        }
    }

    private fun setupRecyclerView() {
        goraAdapter = GoraAdapter(emptyList()) { gora ->
            // TODO: Odpri nov Activity s podrobnostmi o smeri
            Log.d("GoreActivity", "Kliknjena gora: ${gora.naziv}")
        }

        binding.rvGore.apply {
            adapter = goraAdapter
            layoutManager = GridLayoutManager(this@GoreActivity, 2)
        }
    }

    private fun fetchAndFilterGore(gorovjeId: Int) {
        lifecycleScope.launch {
            try {
                // 1. Pridobi VSE gore
                val vseGore = ApiClient.apiService.getGore()
                Log.d("GoreActivity", "Pridobljeno ${vseGore.size} gora.")

                // 2. Filtriraj seznam v kodi
                val filtriraneGore = vseGore.filter { it.gorovjeId == gorovjeId }
                Log.d("GoreActivity", "Po filtriranju ostane ${filtriraneGore.size} gora za gorovjeId=${gorovjeId}.")


                // 3. Prikaži filtrirane gore
                runOnUiThread {
                    goraAdapter.updateData(filtriraneGore)
                }
            } catch (e: Exception) {
                Log.e("GoreActivity", "Napaka pri pridobivanju gora: ", e)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
