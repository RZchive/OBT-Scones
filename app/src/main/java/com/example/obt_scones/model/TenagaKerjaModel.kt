package com.example.obt_scones.model

data class TenagaKerjaModel(
    val id: Long? = null,
    val nik: String?,
    val nama: String?,
    val email: String?,
    val no_hp: String?,
    val jenis_kelamin: String? = "L",
    val tanggal_lahir: String? = null,
    val alamat: String?,
    val pendidikan_terakhir: String?,
    val status_pekerjaan: String? = "Belum Bekerja",
    val foto: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
