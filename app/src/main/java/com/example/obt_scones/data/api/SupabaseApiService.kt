package com.example.obt_scones.data.api

import com.example.obt_scones.model.PelatihanModel
import com.example.obt_scones.model.TenagaKerjaModel
import com.example.obt_scones.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApiService {

    // --- USERS (Register & Login) ---
    @POST("users")
    suspend fun registerUser(@Body user: UserModel): Response<List<UserModel>>

    @GET("users")
    suspend fun getUsers(@Query("email") emailEq: String): Response<List<UserModel>>

    // --- TENAGA KERJA (CRUD) ---
    @GET("tenaga_kerja")
    suspend fun getTenagaKerja(): Response<List<TenagaKerjaModel>>

    @POST("tenaga_kerja")
    suspend fun insertTenagaKerja(@Body data: TenagaKerjaModel): Response<List<TenagaKerjaModel>>

    @PATCH("tenaga_kerja")
    suspend fun updateTenagaKerja(@Query("id") idEq: String, @Body data: TenagaKerjaModel): Response<List<TenagaKerjaModel>>

    @DELETE("tenaga_kerja")
    suspend fun deleteTenagaKerja(@Query("id") idEq: String): Response<Void>

    // --- PELATIHAN (CRUD) ---
    @GET("pelatihan")
    suspend fun getPelatihan(): Response<List<PelatihanModel>>

    @POST("pelatihan")
    suspend fun insertPelatihan(@Body data: PelatihanModel): Response<List<PelatihanModel>>

    @PATCH("pelatihan")
    suspend fun updatePelatihan(@Query("id") idEq: String, @Body data: PelatihanModel): Response<List<PelatihanModel>>

    @DELETE("pelatihan")
    suspend fun deletePelatihan(@Query("id") idEq: String): Response<Void>
}
