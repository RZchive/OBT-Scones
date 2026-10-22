package com.example.obt_scones.auth

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.obt_scones.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inputTanggalLahir.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val dateString = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.inputTanggalLahir.setText(dateString)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.btnSelanjutnya.setOnClickListener {
            val nama = binding.inputNama.text.toString()
            val email = binding.inputEmail.text.toString()
            val tanggalLahir = binding.inputTanggalLahir.text.toString()

            val selectedGenderId = binding.rgJenisKelamin.checkedRadioButtonId
            val jenisKelamin = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            val username = binding.inputUsername.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.inputConfirmPassword.text.toString()

            val sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("NAMA", nama)
            editor.putString("EMAIL", email)
            editor.putString("TANGGAL_LAHIR", tanggalLahir)
            editor.putString("JENIS_KELAMIN", jenisKelamin)
            editor.putString("USERNAME", username)
            editor.putString("PASSWORD", password)
            editor.putString("CONFIRM_PASSWORD", confirmPassword)
            editor.apply()

            val intent = Intent(this, VerifikasiActivity::class.java)
            startActivity(intent)
        }
    }
}
