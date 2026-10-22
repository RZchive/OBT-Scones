package com.example.obt_scones.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.databinding.ActivityLoginBinding
import com.example.obt_scones.pertemuan_3.ThirdResultActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSubmit.setOnClickListener {
            val username = binding.inputNoPengguna.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            val savedUsername = sharedPref.getString("USERNAME", "") ?: ""
            val savedPassword = sharedPref.getString("PASSWORD", "") ?: ""

            if ((username.isNotEmpty() && username == password) ||
                (savedUsername.isNotEmpty() && username == savedUsername && password == savedPassword)
            ) {
                val intent = Intent(this, com.example.obt_scones.MainActivity::class.java)
                startActivity(intent)
            } else {
                android.widget.Toast.makeText(this, "Login Gagal!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
