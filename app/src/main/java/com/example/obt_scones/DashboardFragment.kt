package com.example.obt_scones

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.obt_scones.adapter.TrainingAdapter
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.FragmentDashboardBinding
import com.example.obt_scones.model.TrainingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadPelatihanFromSupabase()
        }

        loadPelatihanFromSupabase()

        binding.btnOpenTenagaKerja.setOnClickListener {
            val intent = Intent(requireContext(), TenagaKerjaActivity::class.java)
            startActivity(intent)
        }

        binding.btnOpenPelatihan.setOnClickListener {
            val intent = Intent(requireContext(), PelatihanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPelatihanFromSupabase() {
        binding.swipeRefreshLayout.isRefreshing = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.getPelatihan()
                withContext(Dispatchers.Main) {
                    binding.swipeRefreshLayout.isRefreshing = false
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
                            Toast.makeText(requireContext(), "Pelatihan: ${selected.title}", Toast.LENGTH_SHORT).show()
                        }
                        binding.rvTraining.apply {
                            layoutManager = GridLayoutManager(requireContext(), 2)
                            this.adapter = adapter
                        }
                    } else {
                        Toast.makeText(requireContext(), "Gagal memuat pelatihan dari Supabase", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
