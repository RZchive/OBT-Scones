package com.example.obt_scones

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.ActivityFormTenagaKerjaBinding
import com.example.obt_scones.model.TenagaKerjaModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormTenagaKerjaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormTenagaKerjaBinding
    private var isEditMode = false
    private var recordId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFormTenagaKerjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DatePicker for Tanggal Lahir
        binding.etTanggalLahir.isFocusable = false
        binding.etTanggalLahir.setOnClickListener {
            showDatePicker(binding.etTanggalLahir)
        }

        recordId = intent.getLongExtra("EXTRA_ID", -1)
        if (recordId != -1L) {
            isEditMode = true
            binding.tvFormTitle.text = "Edit Tenaga Kerja"
            binding.btnDelete.visibility = View.VISIBLE
            binding.etNik.setText(intent.getStringExtra("EXTRA_NIK"))
            binding.etNama.setText(intent.getStringExtra("EXTRA_NAMA"))
            binding.etEmail.setText(intent.getStringExtra("EXTRA_EMAIL"))
            binding.etNoHp.setText(intent.getStringExtra("EXTRA_NOHP"))
            binding.etTanggalLahir.setText(intent.getStringExtra("EXTRA_TANGGALLAHIR"))
            binding.etAlamat.setText(intent.getStringExtra("EXTRA_ALAMAT"))
            binding.etPendidikan.setText(intent.getStringExtra("EXTRA_PENDIDIKAN"))
            binding.etFoto.setText(intent.getStringExtra("EXTRA_FOTO"))
        }

        binding.btnSave.setOnClickListener {
            val nik = binding.etNik.text.toString().trim()
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val noHp = binding.etNoHp.text.toString().trim()
            val tanggalLahir = binding.etTanggalLahir.text.toString().trim()
            val alamat = binding.etAlamat.text.toString().trim()
            val pendidikan = binding.etPendidikan.text.toString().trim()
            val foto = binding.etFoto.text.toString().trim()

            if (nik.isNotEmpty() && nama.isNotEmpty()) {
                val currentTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val data = TenagaKerjaModel(
                    id = if (isEditMode) recordId else null,
                    nik = nik,
                    nama = nama,
                    email = email.ifEmpty { "noemail@example.com" },
                    no_hp = noHp.ifEmpty { "-" },
                    jenis_kelamin = "L",
                    tanggal_lahir = tanggalLahir.ifEmpty { null },
                    alamat = alamat.ifEmpty { "-" },
                    pendidikan_terakhir = pendidikan.ifEmpty { "-" },
                    status_pekerjaan = "Belum Bekerja",
                    foto = foto.ifEmpty { null },
                    created_at = currentTimestamp,
                    updated_at = currentTimestamp
                )

                if (isEditMode) {
                    updateData(data)
                } else {
                    insertData(data)
                }
            } else {
                Toast.makeText(this, "NIK dan Nama wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            deleteData()
        }
    }

    private fun showDatePicker(targetEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            targetEditText.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun insertData(data: TenagaKerjaModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.insertTenagaKerja(data)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FormTenagaKerjaActivity, "Berhasil Menambah Data!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormTenagaKerjaActivity, "Gagal Menambah Data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FormTenagaKerjaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateData(data: TenagaKerjaModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.updateTenagaKerja("eq.$recordId", data)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FormTenagaKerjaActivity, "Berhasil Update Data!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormTenagaKerjaActivity, "Gagal Update Data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FormTenagaKerjaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.deleteTenagaKerja("eq.$recordId")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FormTenagaKerjaActivity, "Berhasil Hapus Data!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormTenagaKerjaActivity, "Gagal Hapus Data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FormTenagaKerjaActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
