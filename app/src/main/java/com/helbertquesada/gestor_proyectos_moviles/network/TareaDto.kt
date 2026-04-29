package com.helbertquesada.gestor_proyectos_moviles.network

import com.google.gson.annotations.SerializedName

// ─── Wrapper para GET /api/tareas ─────────────────────────────────────────────
// Cubre: {"tareas":[...], "total":N}  y  {"data":[...], "total":N}  (Laravel paginate)

data class TareasResponseDto(
    @SerializedName("tareas") val tareas: List<TareaDto>? = null,
    @SerializedName("data")   val data:   List<TareaDto>? = null,
    @SerializedName("total")  val total:  Int = 0
) {
    val items: List<TareaDto>
        get() = tareas ?: data ?: emptyList()
}

// ─── DTO individual ───────────────────────────────────────────────────────────

data class TareaDto(
    @SerializedName("id")              val id: Int,
    @SerializedName("proyecto_id")     val proyectoId: Int?,
    @SerializedName("proyecto_nombre") val proyectoNombre: String?,
    @SerializedName("titulo")          val titulo: String?,
    @SerializedName("nombre")          val nombre: String?,
    @SerializedName("descripcion")     val descripcion: String?,
    @SerializedName("estado")          val estado: String?,
    @SerializedName("prioridad")       val prioridad: String?,
    @SerializedName("fecha_inicio")    val fechaInicio: String?,
    @SerializedName("fecha_fin")       val fechaFin: String?,
    @SerializedName("asignado")        val asignado: String?,
    @SerializedName("asignado_a")      val asignadoA: String?,
    @SerializedName("assigned_to")     val assignedTo: String?,
    @SerializedName("assigned_user")   val assignedUser: String?,
    @SerializedName("user")            val user: UsuarioResumenDto?,
    @SerializedName("created_at")      val createdAt: String?
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
            ?: assignedUser?.takeIf { it.isNotBlank() }

    val estadoNormalizado: String
        get() = when (estado?.lowercase()?.trim()) {
            "en progreso", "en_progreso", "in_progress", "inprogress",
            "progress", "in progress", "en curso" -> "En progreso"
            "completado", "completed", "done", "terminado",
            "finalizado", "cerrado", "resuelto" -> "Completado"
            else -> "Pendiente"
        }
}
