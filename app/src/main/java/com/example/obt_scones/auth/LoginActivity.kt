package com.example.obt_scones.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.MainActivity
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt

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
            // Automatically convert email to lowercase to prevent uppercase/lowercase login issues
            val email = binding.inputNoPengguna.text.toString().trim().lowercase()
            val password = binding.editTextTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = SupabaseClient.apiService.getUsers("eq.$email")
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val users = response.body() ?: emptyList()
                                if (users.isNotEmpty()) {
                                    val user = users[0]
                                    if (user.status != "aktif") {
                                        Toast.makeText(this@LoginActivity, "Akun tidak aktif", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // Verify password using BCrypt (matching Laravel Hash::check) or plaintext fallback
                                        val isPasswordValid = try {
                                            BCrypt.checkpw(password, user.password) || user.password == password
                                        } catch (e: Exception) {
                                            user.password == password
                                        }

                                        if (isPasswordValid) {
                                            val sharedPreferences = getSharedPreferences("SiManDatPrefs", MODE_PRIVATE)
                                            sharedPreferences.edit().apply {
                                                putBoolean("isLogin", true)
                                                putString("userName", user.nama)
                                                putString("userEmail", user.email)
                                                putString("userRole", user.role)
                                            }.apply()

                                            Toast.makeText(this@LoginActivity, "Login berhasil, Selamat datang ${user.nama}!", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this@LoginActivity, "Email atau password salah", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity, "Email atau password salah", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@LoginActivity, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
