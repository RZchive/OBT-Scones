package com.example.obt_scones

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.databinding.ActivityProfilLpkBinding

class ProfilLpkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilLpkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfilLpkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            navigateToDashboard()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToDashboard()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNav()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    private fun setupBottomNav() {
        val skyBlue = resources.getColor(R.color.primary_sky_blue, theme)

        // Highlight Profil as Active
        binding.bottomNav.tvNavProfil.setTextColor(skyBlue)
        binding.bottomNav.ivNavProfil.imageTintList = ColorStateList.valueOf(skyBlue)

        binding.bottomNav.navDashboard.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.bottomNav.navProfil.setOnClickListener {
            // Already here
        }
        binding.bottomNav.navPeserta.setOnClickListener {
            startActivity(Intent(this, PesertaActivity::class.java))
            finish()
        }
    }
}
