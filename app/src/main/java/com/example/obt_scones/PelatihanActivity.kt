package com.example.obt_scones

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.obt_scones.adapter.TrainingAdapter
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.ActivityPelatihanBinding
import com.example.obt_scones.model.TrainingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PelatihanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPelatihanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnTambahPelatihan.setOnClickListener {
            val intent = Intent(this, FormPelatihanActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.getPelatihan()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val list = response.body() ?: emptyList()
                        val modelList = list.map {
                            TrainingModel(
                                title = it.nama_pelatihan ?: "Pelatihan",
                                category = it.jurusan ?: "Umum",
                                quota = "${it.kuota ?: 0} peserta"
                            )
                        }
                        val adapter = TrainingAdapter(modelList) { selected ->
                            val matchedItem = list.find { it.nama_pelatihan == selected.title }
                            if (matchedItem != null) {
                                val intent = Intent(this@PelatihanActivity, FormPelatihanActivity::class.java).apply {
                                    putExtra("EXTRA_ID", matchedItem.id ?: -1L)
                                    putExtra("EXTRA_NAMA", matchedItem.nama_pelatihan ?: "")
                                    putExtra("EXTRA_JENIS", matchedItem.jenis_pelatihan ?: "")
                                    putExtra("EXTRA_JURUSAN", matchedItem.jurusan ?: "")
                                    putExtra("EXTRA_DESKRIPSI", matchedItem.deskripsi ?: "")
                                    putExtra("EXTRA_KUOTA", matchedItem.kuota ?: 20)
                                    putExtra("EXTRA_TANGGALMULAI", matchedItem.tanggal_mulai ?: "")
                                    putExtra("EXTRA_TANGGALSELESAI", matchedItem.tanggal_selesai ?: "")
                                }
                                startActivity(intent)
                            }
                        }
                        binding.rvPelatihan.layoutManager = LinearLayoutManager(this@PelatihanActivity)
                        binding.rvPelatihan.adapter = adapter
                    } else {
                        Toast.makeText(this@PelatihanActivity, "Gagal memuat data pelatihan", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PelatihanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
