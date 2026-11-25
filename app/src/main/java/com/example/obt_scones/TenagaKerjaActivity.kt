package com.example.obt_scones

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.obt_scones.adapter.TenagaKerjaAdapter
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.ActivityTenagaKerjaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TenagaKerjaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTenagaKerjaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTenagaKerjaBinding.inflate(layoutInflater)
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

        binding.btnTambahData.setOnClickListener {
            val intent = Intent(this, FormTenagaKerjaActivity::class.java)
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
                val response = SupabaseClient.apiService.getTenagaKerja()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val list = response.body() ?: emptyList()
                        val adapter = TenagaKerjaAdapter(list) { selected ->
                            val intent = Intent(this@TenagaKerjaActivity, FormTenagaKerjaActivity::class.java).apply {
                                putExtra("EXTRA_ID", selected.id ?: -1L)
                                putExtra("EXTRA_NIK", selected.nik ?: "")
                                putExtra("EXTRA_NAMA", selected.nama ?: "")
                                putExtra("EXTRA_EMAIL", selected.email ?: "")
                                putExtra("EXTRA_NOHP", selected.no_hp ?: "")
                                putExtra("EXTRA_TANGGALLAHIR", selected.tanggal_lahir ?: "")
                                putExtra("EXTRA_ALAMAT", selected.alamat ?: "")
                                putExtra("EXTRA_PENDIDIKAN", selected.pendidikan_terakhir ?: "")
                                putExtra("EXTRA_FOTO", selected.foto ?: "")
                            }
                            startActivity(intent)
                        }
                        binding.rvTenagaKerja.layoutManager = LinearLayoutManager(this@TenagaKerjaActivity)
                        binding.rvTenagaKerja.adapter = adapter
                    } else {
                        Toast.makeText(this@TenagaKerjaActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TenagaKerjaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
