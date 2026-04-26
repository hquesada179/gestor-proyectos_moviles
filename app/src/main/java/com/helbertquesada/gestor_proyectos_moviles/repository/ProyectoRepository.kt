package com.helbertquesada.gestor_proyectos_moviles.repository

import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDto
import com.helbertquesada.gestor_proyectos_moviles.network.RetrofitClient

class ProyectoRepository {
    private val api = RetrofitClient.proyectoApi

    suspend fun getProyectos(): Result<List<ProyectoDto>> = runCatching {
        api.getProyectos()
    }
}
