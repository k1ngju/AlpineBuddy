package com.example.alpinebuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.databinding.ActivitySmeriBinding
import com.example.alpinebuddy.ui.adapters.SmerAdapter
import com.example.alpinebuddy.ui.adapters.SmerInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmeriActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmeriBinding
    private lateinit var smerAdapter: SmerAdapter

    companion object {
        const val EXTRA_GORA_ID = "gora_id"
        const val EXTRA_GORA_NAZIV = "gora_naziv"
        const val EXTRA_GORA_SLIKA_URL = "gora_slika_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmeriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val goraId = intent.getIntExtra(EXTRA_GORA_ID, -1)
        val goraNaziv = intent.getStringExtra(EXTRA_GORA_NAZIV)
        val goraSlikaUrl = intent.getStringExtra(EXTRA_GORA_SLIKA_URL)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.collapsingToolbar.title = goraNaziv ?: "Smeri"
        binding.ivGoraHeader.load(goraSlikaUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
        }

        setupRecyclerView()

        if (goraId != -1) {
            fetchSmeriForGora(goraId)
        }
    }

    private fun setupRecyclerView() {
        smerAdapter = SmerAdapter(emptyList()) { smer ->
            val intent = Intent(this, SmerActivity::class.java)
            intent.putExtra("SMER_ID", smer.smerId)
            startActivity(intent)
        }
        binding.rvSmeri.apply {
            layoutManager = LinearLayoutManager(this@SmeriActivity)
            adapter = smerAdapter
        }
    }

    private fun fetchSmeriForGora(goraId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val smeri = ApiClient.apiService.getSmeri()
                val tezavnosti = ApiClient.apiService.getTezavnosti().associateBy { it.tezavnostId }
                val stili = ApiClient.apiService.getStiliSmeri().associateBy { it.stilId }

                val filteredSmeri = smeri.filter { it.goraId == goraId }

                val smerInfoList = filteredSmeri.map { smer ->
                    SmerInfo(
                        smerId = smer.smerId,
                        ime = smer.ime,
                        dolzina = smer.dolzina,
                        tezavnost = tezavnosti[smer.tezavnostId]?.oznaka,
                        stil = stili[smer.stilId]?.naziv
                    )
                }

                withContext(Dispatchers.Main) {
                    smerAdapter.updateData(smerInfoList)
                }
            } catch (e: Exception) {
                Log.e("SmeriActivity", "Error fetching smeri", e)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
