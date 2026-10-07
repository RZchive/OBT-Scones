package com.example.obt_scones

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        val skyBlue = resources.getColor(R.color.primary_sky_blue, theme)

        // Highlight Dashboard as Active
        binding.bottomNav.tvNavDashboard.setTextColor(skyBlue)
        binding.bottomNav.ivNavDashboard.imageTintList = ColorStateList.valueOf(skyBlue)

        binding.bottomNav.navDashboard.setOnClickListener {
            // Already here
        }
        binding.bottomNav.navProfil.setOnClickListener {
            startActivity(Intent(this, ProfilLpkActivity::class.java))
            finish()
        }
        binding.bottomNav.navPeserta.setOnClickListener {
            startActivity(Intent(this, PesertaActivity::class.java))
            finish()
        }
    }
}
