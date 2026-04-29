package com.helbertquesada.gestor_proyectos_moviles.network

import retrofit2.http.GET
import retrofit2.http.Path

interface ProyectoApiService {

    @GET("api/proyectos")
    suspend fun getProyectos(): List<ProyectoDto>

    @GET("api/proyectos/{id}")
    suspend fun getProyecto(@Path("id") id: Int): ProyectoDetalleDto

    @GET("api/proyectos/{id}/tareas")
    suspend fun getTareasByProyecto(@Path("id") id: Int): ProyectoTareasResponseDto

    @GET("api/proyectos/{id}/sprints")
    suspend fun getSprintsByProyecto(@Path("id") id: Int): ProyectoSprintsResponseDto

    @GET("api/tareas")
    suspend fun getTareas(): TareasResponseDto
}
