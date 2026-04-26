package com.helbertquesada.gestor_proyectos_moviles.network

import retrofit2.http.GET

interface ProyectoApiService {
    @GET("api/proyectos")
    suspend fun getProyectos(): List<ProyectoDto>
}
