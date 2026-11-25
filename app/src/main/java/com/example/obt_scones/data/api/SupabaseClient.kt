package com.example.obt_scones.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {
    private const val BASE_URL = "https://ewzkasurmywczoyvukag.supabase.co/rest/v1/"
    private const val API_KEY = "sb_publishable__tu43IwxMGF1MJ21i_neog_fZDJ6uuG"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer $API_KEY")
                .header("Accept-Profile", "project")
                .header("Content-Profile", "project")
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        })
        .build()

    val apiService: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}
