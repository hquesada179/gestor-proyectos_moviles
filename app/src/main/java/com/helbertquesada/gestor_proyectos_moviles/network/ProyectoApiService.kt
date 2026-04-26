package com.helbertquesada.gestor_proyectos_moviles.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ProyectoApiService {

    @GET("api/proyectos")
    suspend fun getProyectos(): List<ProyectoDto>

    @GET("api/proyectos/{id}")
    suspend fun getProyecto(@Path("id") id: Int): ProyectoDetalleDto
}
