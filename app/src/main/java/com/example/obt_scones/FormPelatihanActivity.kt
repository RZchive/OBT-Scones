package com.example.obt_scones

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obt_scones.data.api.SupabaseClient
import com.example.obt_scones.databinding.ActivityFormPelatihanBinding
import com.example.obt_scones.model.PelatihanModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormPelatihanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormPelatihanBinding
    private var isEditMode = false
    private var recordId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFormPelatihanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup Spinner for Jenis Pelatihan (APBD / APBN)
        val jenisOptions = arrayOf("APBD", "APBN")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, jenisOptions)
        binding.spinnerJenis.adapter = spinnerAdapter

        // DatePickers for Tanggal Mulai and Tanggal Selesai
        binding.etTanggalMulai.isFocusable = false
        binding.etTanggalMulai.setOnClickListener { showDatePicker(binding.etTanggalMulai) }

        binding.etTanggalSelesai.isFocusable = false
        binding.etTanggalSelesai.setOnClickListener { showDatePicker(binding.etTanggalSelesai) }

        recordId = intent.getLongExtra("EXTRA_ID", -1)
        if (recordId != -1L) {
            isEditMode = true
            binding.tvFormTitle.text = "Edit Pelatihan"
            binding.btnDeletePelatihan.visibility = View.VISIBLE
            binding.etNamaPelatihan.setText(intent.getStringExtra("EXTRA_NAMA"))
            val jenis = intent.getStringExtra("EXTRA_JENIS") ?: "APBD"
            val jenisIndex = if (jenis == "APBN") 1 else 0
            binding.spinnerJenis.setSelection(jenisIndex)
            binding.etJurusan.setText(intent.getStringExtra("EXTRA_JURUSAN"))
            binding.etDeskripsi.setText(intent.getStringExtra("EXTRA_DESKRIPSI"))
            binding.etKuota.setText(intent.getIntExtra("EXTRA_KUOTA", 20).toString())
            binding.etTanggalMulai.setText(intent.getStringExtra("EXTRA_TANGGALMULAI"))
            binding.etTanggalSelesai.setText(intent.getStringExtra("EXTRA_TANGGALSELESAI"))
        }

        binding.btnSavePelatihan.setOnClickListener {
            val nama = binding.etNamaPelatihan.text.toString().trim()
            val jenis = binding.spinnerJenis.selectedItem.toString()
            val jurusan = binding.etJurusan.text.toString().trim()
            val deskripsi = binding.etDeskripsi.text.toString().trim()
            val kuotaStr = binding.etKuota.text.toString().trim()
            val tanggalMulai = binding.etTanggalMulai.text.toString().trim()
            val tanggalSelesai = binding.etTanggalSelesai.text.toString().trim()

            if (nama.isNotEmpty() && jurusan.isNotEmpty()) {
                val kuota = kuotaStr.toIntOrNull() ?: 20
                val currentTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val data = PelatihanModel(
                    id = if (isEditMode) recordId else null,
                    lpk_id = 1,
                    nama_pelatihan = nama,
                    jenis_pelatihan = jenis,
                    jurusan = jurusan,
                    deskripsi = deskripsi.ifEmpty { "-" },
                    kuota = kuota,
                    status = "dibuka",
                    tanggal_mulai = tanggalMulai.ifEmpty { null },
                    tanggal_selesai = tanggalSelesai.ifEmpty { null },
                    created_at = currentTimestamp,
                    updated_at = currentTimestamp
                )

                if (isEditMode) {
                    updateData(data)
                } else {
                    insertData(data)
                }
            } else {
                Toast.makeText(this, "Nama dan Jurusan wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDeletePelatihan.setOnClickListener {
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

    private fun insertData(data: PelatihanModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.insertPelatihan(data)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FormPelatihanActivity, "Berhasil Menambah Pelatihan!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormPelatihanActivity, "Gagal Menambah Pelatihan", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FormPelatihanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateData(data: PelatihanModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.updatePelatihan("eq.$recordId", data)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FormPelatihanActivity, "Berhasil Update Pelatihan!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormPelatihanActivity, "Gagal Update Pelatihan", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FormPelatihanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.apiService.deletePelatihan("eq.$recordId")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FormPelatihanActivity, "Berhasil Hapus Pelatihan!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormPelatihanActivity, "Gagal Hapus Pelatihan", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FormPelatihanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
