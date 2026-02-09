package com.example.alpinebuddy

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.alpinebuddy.data.ApiClient
import com.example.alpinebuddy.data.SmerRead
import com.example.alpinebuddy.data.StilSmeriRead
import com.example.alpinebuddy.data.TezavnostRead
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmerActivity : AppCompatActivity() {

    private lateinit var smerTitle: TextView
    private lateinit var smerDolzina: TextView
    private lateinit var smerTezavnost: TextView
    private lateinit var smerStil: TextView
    private lateinit var smerOpis: TextView
    private lateinit var skicaImage1: ImageView
    private lateinit var skicaImage2: ImageView
    private lateinit var skiceTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smer)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        smerTitle = findViewById(R.id.smerTitle)
        smerDolzina = findViewById(R.id.smerDolzina)
        smerTezavnost = findViewById(R.id.smerTezavnost)
        smerStil = findViewById(R.id.smerStil)
        smerOpis = findViewById(R.id.smerOpis)
        skicaImage1 = findViewById(R.id.skicaImage1)
        skicaImage2 = findViewById(R.id.skicaImage2)
        skiceTitle = findViewById(R.id.skiceTitle)


        val smerId = intent.getIntExtra("SMER_ID", -1)
        if (smerId != -1) {
            fetchSmerDetails(smerId)
        }
    }

    private fun fetchSmerDetails(smerId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val smer = ApiClient.apiService.getSmer(smerId)
                val tezavnost = smer.tezavnostId?.let { ApiClient.apiService.getTezavnost(it) }
                val stil = smer.stilId?.let { ApiClient.apiService.getStilSmeri(it) }

                withContext(Dispatchers.Main) {
                    displaySmerDetails(smer, tezavnost, stil)
                }
            } catch (e: Exception) {
                Log.e("SmerActivity", "Error fetching smer details", e)
            }
        }
    }

    private fun displaySmerDetails(smer: SmerRead, tezavnost: TezavnostRead?, stil: StilSmeriRead?) {
        smerTitle.text = smer.ime ?: "Neznano ime"
        supportActionBar?.title = smer.ime ?: "Podrobnosti smeri"

        smerDolzina.text = "Dolžina: ${smer.dolzina?.toString() ?: "neznana"} m"
        smerTezavnost.text = "Težavnost: ${tezavnost?.oznaka ?: "neznana"}"
        smerStil.text = "Stil: ${stil?.naziv ?: "neznan"}"

        smer.opis?.let {
            smerOpis.text = it.replace("\\n", "\n")
        } ?: run {
            smerOpis.visibility = View.GONE
            findViewById<TextView>(R.id.opisTitle).visibility = View.GONE
        }


        val hasSkica1 = !smer.skicaUrl1.isNullOrEmpty()
        val hasSkica2 = !smer.skicaUrl2.isNullOrEmpty()

        if (hasSkica1) {
            skicaImage1.visibility = View.VISIBLE
            skicaImage1.load(smer.skicaUrl1) {
                placeholder(R.drawable.ic_launcher_background) // Optional placeholder
                error(R.drawable.ic_launcher_foreground) // Optional error image
            }
        }

        if (hasSkica2) {
            skicaImage2.visibility = View.VISIBLE
            skicaImage2.load(smer.skicaUrl2) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }
        }
        
        if (!hasSkica1 && !hasSkica2) {
            skiceTitle.visibility = View.GONE
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