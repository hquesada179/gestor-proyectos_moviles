package com.helbertquesada.gestor_proyectos_moviles.network

import com.google.gson.annotations.SerializedName

// ─── Wrapper que devuelve el endpoint GET /api/proyectos/{id}/tareas ──────────

data class ProyectoTareasResponseDto(
    @SerializedName("proyecto_id")     val proyectoId: Int?,
    @SerializedName("proyecto_nombre") val proyectoNombre: String?,
    @SerializedName("total")           val total: Int = 0,
    @SerializedName("tareas")          val tareas: List<ProyectoTareaDto> = emptyList()
)

// ─── Tarea individual ─────────────────────────────────────────────────────────

data class ProyectoTareaDto(
    @SerializedName("id")            val id: Int,
    @SerializedName("proyecto_id")   val proyectoId: Int?,
    // título: "titulo" o "nombre" según el backend
    @SerializedName("titulo")        val titulo: String?,
    @SerializedName("nombre")        val nombre: String?,
    @SerializedName("descripcion")   val descripcion: String?,
    @SerializedName("estado")        val estado: String?,
    @SerializedName("prioridad")     val prioridad: String?,
    @SerializedName("fecha_inicio")  val fechaInicio: String?,
    @SerializedName("fecha_fin")     val fechaFin: String?,
    // asignado: cubre "asignado", "asignado_a", "assigned_to" y objeto anidado
    @SerializedName("asignado")      val asignado: String?,
    @SerializedName("asignado_a")    val asignadoA: String?,
    @SerializedName("assigned_to")   val assignedTo: String?,
    @SerializedName("user")          val user: UsuarioResumenDto?,
    @SerializedName("created_at")    val createdAt: String?
) {
    val displayTitle: String
        get() = titulo?.takeIf { it.isNotBlank() }
            ?: nombre?.takeIf { it.isNotBlank() }
            ?: "Sin título"

    val displayAsignado: String?
        get() = user?.name?.takeIf { it.isNotBlank() }
            ?: asignado?.takeIf { it.isNotBlank() }
            ?: asignadoA?.takeIf { it.isNotBlank() }
            ?: assignedTo?.takeIf { it.isNotBlank() }
}

data class UsuarioResumenDto(
    @SerializedName("id")   val id: Int?,
    @SerializedName("name") val name: String?
)
