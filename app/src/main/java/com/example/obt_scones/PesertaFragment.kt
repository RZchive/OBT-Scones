package com.example.obt_scones

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.obt_scones.adapter.TenagaKerjaAdapter
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.FragmentPesertaBinding
import com.example.obt_scones.model.TenagaKerjaModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PesertaFragment : Fragment() {
    private var _binding: FragmentPesertaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPesertaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadPesertaFromSupabase()
        }

        binding.btnAddPeserta.setOnClickListener {
            insertPesertaToSupabase()
        }

        loadPesertaFromSupabase()
    }

    private fun loadPesertaFromSupabase() {
        binding.swipeRefreshLayout.isRefreshing = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.getTenagaKerja()
                withContext(Dispatchers.Main) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (response.isSuccessful) {
                        val dataList = response.body() ?: emptyList()
                        val adapter = TenagaKerjaAdapter(dataList) { selected ->
                            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                                putExtra("EXTRA_NAMA", selected.nama)
                                putExtra("EXTRA_NIK", selected.nik)
                            }
                            startActivity(intent)
                        }
                        binding.rvPeserta.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvPeserta.adapter = adapter
                    } else {
                        Toast.makeText(requireContext(), "Gagal memuat data dari Supabase", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun insertPesertaToSupabase() {
        val newPeserta = TenagaKerjaModel(
            nik = "147101${System.currentTimeMillis().toString().takeLast(10)}",
            nama = "Peserta Baru ${System.currentTimeMillis().toString().takeLast(3)}",
            email = "peserta@example.com",
            no_hp = "081299887766",
            jenis_kelamin = "L",
            alamat = "Pekanbaru, Riau",
            pendidikan_terakhir = "S1",
            status_pekerjaan = "Belum Bekerja"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.insertTenagaKerja(newPeserta)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Berhasil Tambah Peserta!", Toast.LENGTH_SHORT).show()
                        loadPesertaFromSupabase()
                    } else {
                        Toast.makeText(requireContext(), "Gagal Tambah Peserta", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
