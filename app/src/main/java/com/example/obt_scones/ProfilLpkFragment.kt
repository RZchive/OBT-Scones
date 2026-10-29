package com.example.obt_scones

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.obt_scones.databinding.FragmentProfilLpkBinding

class ProfilLpkFragment : Fragment() {
    private var _binding: FragmentProfilLpkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilLpkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1500)
        }

        // Set initial switch state based on current night mode
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        binding.switchDarkMode.isChecked = (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Smooth transition animation when restarting activity while keeping the current tab (Profil LPK)
            activity?.let {
                it.finish()
                val intent = Intent(it, it::class.java).apply {
                    putExtra("OPEN_TAB", "profil")
                }
                it.startActivity(intent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    it.overrideActivityTransition(
                        Activity.OVERRIDE_TRANSITION_CLOSE,
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                } else {
                    @Suppress("DecodedDeprecation", "DEPRECATION")
                    it.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
