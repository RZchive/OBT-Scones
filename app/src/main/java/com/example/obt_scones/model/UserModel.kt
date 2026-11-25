package com.example.obt_scones.model

data class UserModel(
    val id: Long? = null,
    val nama: String,
    val email: String,
    val password: String,
    val role: String = "lpk",
    val status: String = "aktif",
    val created_at: String? = null,
    val updated_at: String? = null
)
