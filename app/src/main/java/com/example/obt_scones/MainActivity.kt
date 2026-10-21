package com.example.obt_scones

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.obt_scones.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val logoutHandler = Handler(Looper.getMainLooper())
    private val logoutRunnable = Runnable {
        val sharedPreferences = getSharedPreferences("SiManDatPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLogin", false).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Default Fragment
        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.nav_profil -> {
                    replaceFragment(ProfilLpkFragment())
                    true
                }
                R.id.nav_peserta -> {
                    replaceFragment(PesertaFragment())
                    true
                }
                R.id.nav_developer -> {
                    replaceFragment(DeveloperFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        logoutHandler.removeCallbacks(logoutRunnable)
    }

    override fun onStop() {
        super.onStop()
        logoutHandler.postDelayed(logoutRunnable, 60000)
    }
}
