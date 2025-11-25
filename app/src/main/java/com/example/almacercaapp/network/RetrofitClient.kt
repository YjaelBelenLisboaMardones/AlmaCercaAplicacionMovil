package com.example.almacercaapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 👇 AQUÍ ESTÁ TU URL DE RAILWAY YA CONFIGURADA
    private const val BASE_URL = "https://almacercabackend-production.up.railway.app/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}