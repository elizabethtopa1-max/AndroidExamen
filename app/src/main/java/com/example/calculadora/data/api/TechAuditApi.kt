package com.example.calculadora.data.api

import com.example.calculadora.data.entities.Equipo
import com.example.calculadora.data.entities.Laboratorio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TechAuditApi {

    @GET("laboratorios")
    suspend fun getLaboratorios(): Response<List<Laboratorio>>

    @POST("laboratorios")
    suspend fun postLaboratorio(@Body laboratorio: Laboratorio): Response<Laboratorio>

    @GET("equipos")
    suspend fun getEquipos(): Response<List<Equipo>>

    @POST("equipos")
    suspend fun postEquipo(@Body equipo: Equipo): Response<Equipo>
}
