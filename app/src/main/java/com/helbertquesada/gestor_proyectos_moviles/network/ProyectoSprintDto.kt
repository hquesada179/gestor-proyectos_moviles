package com.helbertquesada.gestor_proyectos_moviles.network

import com.google.gson.annotations.SerializedName

// ─── Wrapper que devuelve GET /api/proyectos/{id}/sprints ─────────────────────

data class ProyectoSprintsResponseDto(
    @SerializedName("proyecto_id")     val proyectoId: Int?,
    @SerializedName("proyecto_nombre") val proyectoNombre: String?,
    @SerializedName("total")           val total: Int = 0,
    @SerializedName("sprints")         val sprints: List<ProyectoSprintDto> = emptyList()
)

// ─── Sprint individual ────────────────────────────────────────────────────────

data class ProyectoSprintDto(
    @SerializedName("id")           val id: Int,
    @SerializedName("proyecto_id")  val proyectoId: Int?,
    @SerializedName("nombre")       val nombre: String?,
    @SerializedName("descripcion")  val descripcion: String?,
    @SerializedName("estado")       val estado: String?,
    @SerializedName("fecha_inicio") val fechaInicio: String?,
    @SerializedName("fecha_fin")    val fechaFin: String?,
    @SerializedName("created_at")   val createdAt: String?
) {
    val displayNombre: String
        get() = nombre?.takeIf { it.isNotBlank() } ?: "Sprint #$id"
}
