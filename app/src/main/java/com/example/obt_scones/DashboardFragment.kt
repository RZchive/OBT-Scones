package com.example.obt_scones

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
import com.example.obt_scones.databinding.FragmentDashboardBinding
import com.example.obt_scones.model.TrainingModel

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
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1500)
        }

        // Setup RecyclerView with CustomAdapter and GridLayoutManager (Grid)
        val trainingList = listOf(
            TrainingModel("Web Laravel", "Teknik Informasi", "20 peserta"),
            TrainingModel("Desain Grafis", "Multimedia", "15 peserta"),
            TrainingModel("Teknisi Komputer", "Hardware", "25 peserta"),
            TrainingModel("Digital Marketing", "Bisnis", "30 peserta")
        )

        val adapter = TrainingAdapter(trainingList) { selected ->
            Toast.makeText(requireContext(), "Pelatihan: ${selected.title}", Toast.LENGTH_SHORT).show()
        }

        binding.rvTraining.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
