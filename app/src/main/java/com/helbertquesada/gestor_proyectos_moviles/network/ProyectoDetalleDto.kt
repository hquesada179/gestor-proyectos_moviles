package com.helbertquesada.gestor_proyectos_moviles.network

import com.google.gson.annotations.SerializedName

data class TareaResumenDto(
    @SerializedName("id")        val id: Int,
    @SerializedName("titulo")    val titulo: String?,
    @SerializedName("nombre")    val nombre: String?,
    @SerializedName("estado")    val estado: String?,
    @SerializedName("prioridad") val prioridad: String?
) {
    val displayName: String get() = titulo ?: nombre ?: "Sin título"
}

data class SprintResumenDto(
    @SerializedName("id")          val id: Int,
    @SerializedName("nombre")      val nombre: String?,
    @SerializedName("estado")      val estado: String?,
    @SerializedName("fecha_inicio") val fechaInicio: String?,
    @SerializedName("fecha_fin")    val fechaFin: String?
)

data class ProyectoDetalleDto(
    @SerializedName("id")                 val id: Int,
    @SerializedName("nombre")             val nombre: String,
    @SerializedName("descripcion")        val descripcion: String?,
    @SerializedName("estado")             val estado: String?,
    @SerializedName("fecha_inicio")       val fechaInicio: String?,
    @SerializedName("fecha_fin_estimada") val fechaFinEstimada: String?,
    @SerializedName("tasks_count")        val tasksCount: Int = 0,
    @SerializedName("sprints_count")      val sprintsCount: Int = 0,
    // Populated only if the backend eager-loads relationships
    @SerializedName("tareas")             val tareas: List<TareaResumenDto>? = null,
    @SerializedName("sprints")            val sprints: List<SprintResumenDto>? = null,
    @SerializedName("created_at")         val createdAt: String? = null,
    @SerializedName("updated_at")         val updatedAt: String? = null
)
