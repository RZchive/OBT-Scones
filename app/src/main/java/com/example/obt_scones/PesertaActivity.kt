package com.example.obt_scones

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.databinding.ActivityPesertaBinding

class PesertaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPesertaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPesertaBinding.inflate(layoutInflater)
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

        binding.cardPeserta1.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
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

        // Highlight Peserta as Active
        binding.bottomNav.tvNavPeserta.setTextColor(skyBlue)
        binding.bottomNav.ivNavPeserta.imageTintList = ColorStateList.valueOf(skyBlue)

        binding.bottomNav.navDashboard.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.bottomNav.navProfil.setOnClickListener {
            startActivity(Intent(this, ProfilLpkActivity::class.java))
            finish()
        }
        binding.bottomNav.navPeserta.setOnClickListener {
            // Already here
        }
    }
}
