package com.example.obt_scones.model

data class PelatihanModel(
    val id: Long? = null,
    val lpk_id: Long? = 1,
    val nama_pelatihan: String?,
    val jenis_pelatihan: String?,
    val jurusan: String?,
    val deskripsi: String?,
    val kuota: Int?,
    val status: String? = "dibuka",
    val tanggal_mulai: String? = null,
    val tanggal_selesai: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)
