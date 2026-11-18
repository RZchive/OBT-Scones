package com.example.obt_scones.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.obt_scones.auth.LoginActivity
import com.example.obt_scones.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragments = listOf(
            Onboarding1Fragment(),
            Onboarding2Fragment(),
            Onboarding3Fragment()
        )

        val adapter = OnboardingAdapter(this, fragments)
        binding.viewPagerOnboard.adapter = adapter

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPagerOnboard.currentItem
            if (currentItem < fragments.size - 1) {
                binding.viewPagerOnboard.currentItem = currentItem + 1
            } else {
                // Mark onboarding as completed
                val sharedPrefs = getSharedPreferences("SiManDatPrefs", MODE_PRIVATE)
                sharedPrefs.edit().putBoolean("isOnboarded", true).apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.viewPagerOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == fragments.size - 1) {
                    binding.btnNext.text = "Mulai Masuk"
                } else {
                    binding.btnNext.text = "Lanjut"
                }
            }
        })
    }
}
