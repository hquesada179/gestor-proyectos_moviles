package com.helbertquesada.gestor_proyectos_moviles.network

import com.google.gson.annotations.SerializedName

data class ProyectoDto(
    @SerializedName("id")                  val id: Int,
    @SerializedName("nombre")              val nombre: String,
    @SerializedName("descripcion")         val descripcion: String?,
    @SerializedName("estado")              val estado: String?,
    @SerializedName("fecha_inicio")        val fechaInicio: String?,
    @SerializedName("fecha_fin_estimada")  val fechaFinEstimada: String?,
    @SerializedName("tasks_count")         val tasksCount: Int = 0,
    @SerializedName("sprints_count")       val sprintsCount: Int = 0
)
