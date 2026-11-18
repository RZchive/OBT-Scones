package com.example.obt_scones

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.auth.LoginActivity
import com.example.obt_scones.databinding.ActivitySplashBinding
import com.example.obt_scones.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            checkFlowStatus()
        }, 2000)
    }

    private fun checkFlowStatus() {
        val sharedPreferences = getSharedPreferences("SiManDatPrefs", MODE_PRIVATE)
        val isOnboarded = sharedPreferences.getBoolean("isOnboarded", false)
        val isLogin = sharedPreferences.getBoolean("isLogin", false)

        val intent = if (!isOnboarded) {
            Intent(this, OnboardingActivity::class.java)
        } else if (isLogin) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
