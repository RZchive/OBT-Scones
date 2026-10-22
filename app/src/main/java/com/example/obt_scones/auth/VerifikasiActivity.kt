package com.example.obt_scones.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.obt_scones.databinding.ActivityVerifikasiBinding

class VerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val nama = sharedPref.getString("NAMA", "") ?: ""
        val email = sharedPref.getString("EMAIL", "") ?: ""
        val tanggalLahir = sharedPref.getString("TANGGAL_LAHIR", "") ?: ""
        val jenisKelamin = sharedPref.getString("JENIS_KELAMIN", "") ?: ""
        val username = sharedPref.getString("USERNAME", "") ?: ""
        val password = sharedPref.getString("PASSWORD", "") ?: ""
        val confirmPassword = sharedPref.getString("CONFIRM_PASSWORD", "") ?: ""

        binding.tvValNama.text = "Nama: $nama"
        binding.tvValEmail.text = "Email: $email"
        binding.tvValTanggalLahir.text = "Tanggal Lahir: $tanggalLahir"
        binding.tvValJenisKelamin.text = "Jenis Kelamin: $jenisKelamin"
        binding.tvValUsername.text = "Username: $username"
        binding.tvValPassword.text = "Password: $password"
        binding.tvValConfirmPassword.text = "Confirm Password: $confirmPassword"

        binding.btnSubmitVerifikasi.setOnClickListener {
            if (nama.isEmpty() || email.isEmpty() || tanggalLahir.isEmpty() ||
                jenisKelamin.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
            ) {
                binding.tvStatusValidation.text = "Validasi Gagal: Semua inputan tidak boleh kosong!"
                binding.tvStatusValidation.visibility = View.VISIBLE
                binding.btnKembali.visibility = View.VISIBLE
                binding.layoutSuccess.visibility = View.GONE
            } else if (password != confirmPassword) {
                binding.tvStatusValidation.text = "Validasi Gagal: Password dan Confirm Password tidak sama!"
                binding.tvStatusValidation.visibility = View.VISIBLE
                binding.btnKembali.visibility = View.VISIBLE
                binding.layoutSuccess.visibility = View.GONE
            } else {
                binding.tvStatusValidation.visibility = View.GONE
                binding.btnKembali.visibility = View.GONE
                binding.layoutSuccess.visibility = View.VISIBLE
            }
        }

        binding.btnKembali.setOnClickListener {
            finish()
        }

        binding.btnKeLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
