package com.example.calculadora.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://65f8a4c2df151452460f1d25.mockapi.io/api/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TechAuditApi by lazy {
        retrofit.create(TechAuditApi::class.java)
    }
}
