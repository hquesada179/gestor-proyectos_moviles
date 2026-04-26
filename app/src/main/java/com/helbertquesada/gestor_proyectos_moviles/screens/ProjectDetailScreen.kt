package com.helbertquesada.gestor_proyectos_moviles.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDetalleDto
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoSprintDto
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoTareaDto
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentAmber
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlue
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlueLight
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentViolet
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentVioletLight
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.BorderDefault
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkBackground
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkCard
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkCardElevated
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.ErrorColor
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.SuccessColor
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextDisabled
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextPrimary
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextSecondary
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.ProyectoDetalleUiState
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.ProyectoDetalleViewModel
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.SprintsUiState
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.TareasUiState

// ─── Estado → color ───────────────────────────────────────────────────────────

private fun estadoColor(estado: String?): Color = when (estado?.lowercase()?.trim()) {
    "activo", "en progreso", "en_progreso", "active", "in_progress" -> SuccessColor
    "completado", "completada", "finalizado", "done"                 -> AccentBlue
    "pausado", "pausada", "en pausa", "paused"                       -> AccentAmber
    "cancelado", "cancelada", "archivado"                            -> ErrorColor
    "pendiente", "pending"                                           -> AccentVioletLight
    else                                                              -> AccentVioletLight
}

private fun prioridadColor(prioridad: String?): Color = when (prioridad?.lowercase()?.trim()) {
    "alta", "high", "urgente", "critica", "crítica" -> ErrorColor
    "media", "medium", "normal"                      -> AccentAmber
    "baja", "low"                                    -> SuccessColor
    else                                             -> TextDisabled
}

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun ProjectDetailScreen(
    projectId: Int,
    onBack: () -> Unit,
    viewModel: ProyectoDetalleViewModel = viewModel(factory = ProyectoDetalleViewModel.factory(projectId))
) {
    val uiState       by viewModel.uiState.collectAsState()
    val tareasUiState  by viewModel.tareasUiState.collectAsState()
    val sprintsUiState by viewModel.sprintsUiState.collectAsState()

    Scaffold(containerColor = DarkBackground) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(innerPadding)
        ) {
            DetailHeader(onBack = onBack)

            when (uiState) {
                is ProyectoDetalleUiState.Loading ->
                    DetailLoading()

                is ProyectoDetalleUiState.Error ->
                    DetailError(
                        message = (uiState as ProyectoDetalleUiState.Error).message,
                        onRetry = { viewModel.reload() }
                    )

                is ProyectoDetalleUiState.Success ->
                    DetailContent(
                        proyecto       = (uiState as ProyectoDetalleUiState.Success).proyecto,
                        tareasUiState  = tareasUiState,
                        sprintsUiState = sprintsUiState,
                        onRetryTareas  = { viewModel.loadTareas() },
                        onRetrySprints = { viewModel.loadSprints() }
                    )
            }
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun DetailHeader(onBack: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBackground)
                .padding(horizontal = 4.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextPrimary, modifier = Modifier.size(22.dp))
            }
            Box(
                modifier = Modifier.size(28.dp).clip(RoundedCornerShape(7.dp)).background(AccentViolet),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Folder, null, tint = Color.White, modifier = Modifier.size(15.dp)) }
            Text(
                "Detalle de Proyecto",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

// ─── Loading / Error de proyecto ─────────────────────────────────────────────

@Composable
private fun DetailLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
            CircularProgressIndicator(color = AccentBlue, modifier = Modifier.size(36.dp), strokeWidth = 2.5.dp)
            Text("Cargando proyecto...", color = TextSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
private fun DetailError(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Surface(color = DarkCard, shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(36.dp))
                Text("No se pudo cargar el proyecto", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(message, color = TextSecondary, fontSize = 12.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Reintentar", fontSize = 13.sp)
                }
            }
        }
    }
}

// ─── Content ─────────────────────────────────────────────────────────────────

@Composable
private fun DetailContent(
    proyecto: ProyectoDetalleDto,
    tareasUiState: TareasUiState,
    sprintsUiState: SprintsUiState,
    onRetryTareas: () -> Unit,
    onRetrySprints: () -> Unit
) {
    val accentColor = estadoColor(proyecto.estado)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // ── Hero banner ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF0D1E4A), Color(0xFF1A1060))))
                .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(accentColor))
                    EstadoBadge(estado = proyecto.estado, color = accentColor)
                }
                Spacer(Modifier.height(10.dp))
                Text(proyecto.nombre, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)
                if (!proyecto.descripcion.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(proyecto.descripcion, color = TextSecondary, fontSize = 13.sp, lineHeight = 19.sp)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Stats row ─────────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatChip(Modifier.weight(1f), Icons.Filled.CheckBox,                "TAREAS",  "${proyecto.tasksCount}",   AccentBlue)
            StatChip(Modifier.weight(1f), Icons.AutoMirrored.Filled.Assignment, "SPRINTS", "${proyecto.sprintsCount}", AccentVioletLight)
        }

        Spacer(Modifier.height(16.dp))

        // ── Fechas ────────────────────────────────────────────────────
        if (!proyecto.fechaInicio.isNullOrBlank() || !proyecto.fechaFinEstimada.isNullOrBlank()) {
            Surface(modifier = Modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionLabel(Icons.Filled.CalendarToday, "FECHAS")
                    proyecto.fechaInicio?.takeIf { it.isNotBlank() }?.let { DateRow("Inicio", it.take(10)) }
                    proyecto.fechaFinEstimada?.takeIf { it.isNotBlank() }?.let { DateRow("Fin estimado", it.take(10)) }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── Info extra ────────────────────────────────────────────────
        if (!proyecto.createdAt.isNullOrBlank()) {
            Surface(modifier = Modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel(Icons.Filled.Info, "INFORMACIÓN")
                    DateRow("Creado", proyecto.createdAt.take(10))
                    proyecto.updatedAt?.takeIf { it.isNotBlank() }?.let { DateRow("Actualizado", it.take(10)) }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // ── Tareas del proyecto (endpoint dedicado) ───────────────────
        TareasSection(tareasUiState = tareasUiState, onRetry = onRetryTareas)

        Spacer(Modifier.height(16.dp))

        // ── Sprints del proyecto (endpoint dedicado) ──────────────────
        SprintsSection(sprintsUiState = sprintsUiState, onRetry = onRetrySprints)

        Spacer(Modifier.height(28.dp))
    }
}

// ─── Sección de tareas ────────────────────────────────────────────────────────

@Composable
private fun TareasSection(tareasUiState: TareasUiState, onRetry: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        SectionLabel(Icons.Filled.CheckBox, "TAREAS DEL PROYECTO")
        Spacer(Modifier.weight(1f))
        if (tareasUiState is TareasUiState.Error) {
            TextButton(onClick = onRetry, contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
                Text("Reintentar", color = AccentBlue, fontSize = 11.sp)
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    when (tareasUiState) {
        is TareasUiState.Loading -> SubsectionLoading("Cargando tareas...")
        is TareasUiState.Error   -> SubsectionError(tareasUiState.message, onRetry)
        is TareasUiState.Success ->
            if (tareasUiState.tareas.isEmpty()) SubsectionEmpty("Este proyecto aún no tiene tareas registradas.", Icons.Filled.CheckBox, AccentBlue)
            else TareasList(tareasUiState.tareas)
    }
}

// ─── Sección de sprints ───────────────────────────────────────────────────────

@Composable
private fun SprintsSection(sprintsUiState: SprintsUiState, onRetry: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        SectionLabel(Icons.AutoMirrored.Filled.Assignment, "SPRINTS DEL PROYECTO")
        Spacer(Modifier.weight(1f))
        if (sprintsUiState is SprintsUiState.Error) {
            TextButton(onClick = onRetry, contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
                Text("Reintentar", color = AccentBlue, fontSize = 11.sp)
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    when (sprintsUiState) {
        is SprintsUiState.Loading -> SubsectionLoading("Cargando sprints...")
        is SprintsUiState.Error   -> SubsectionError(sprintsUiState.message, onRetry)
        is SprintsUiState.Success ->
            if (sprintsUiState.sprints.isEmpty()) SubsectionEmpty("Este proyecto aún no tiene sprints registrados.", Icons.AutoMirrored.Filled.Assignment, AccentVioletLight)
            else SprintsList(sprintsUiState.sprints)
    }
}

// ─── Estados compartidos de subsecciones ─────────────────────────────────────

@Composable
private fun SubsectionLoading(label: String) {
    Surface(modifier = Modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(14.dp)) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CircularProgressIndicator(color = AccentBlue, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            Text(label, color = TextSecondary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun SubsectionEmpty(message: String, icon: ImageVector, accent: Color) {
    Surface(modifier = Modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(14.dp)) {
        Box(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(accent.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) { Icon(icon, null, tint = accent.copy(alpha = 0.5f), modifier = Modifier.size(22.dp)) }
                Text(message, color = TextSecondary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun SubsectionError(message: String, onRetry: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ErrorColor.copy(alpha = 0.06f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, ErrorColor.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(20.dp))
            Text(message, color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1f), maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

// ─── Lista de tareas ──────────────────────────────────────────────────────────

@Composable
private fun TareasList(tareas: List<ProyectoTareaDto>) {
    Surface(modifier = Modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(4.dp)) {
            tareas.forEachIndexed { index, tarea ->
                TareaItem(tarea)
                if (index < tareas.lastIndex)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun TareaItem(tarea: ProyectoTareaDto) {
    val estadoAccent    = estadoColor(tarea.estado)
    val prioridadAccent = prioridadColor(tarea.prioridad)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(estadoAccent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.CheckCircle, null, tint = estadoAccent, modifier = Modifier.size(18.dp)) }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(tarea.displayTitle, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                tarea.estado?.let { MiniChip(it.replaceFirstChar { c -> c.uppercase() }, estadoAccent) }
                tarea.prioridad?.let { MiniChip(it.replaceFirstChar { c -> c.uppercase() }, prioridadAccent) }
            }
            tarea.displayAsignado?.let { asignado ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Person, null, tint = TextDisabled, modifier = Modifier.size(11.dp))
                    Text(asignado, color = TextDisabled, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            val fechas = listOfNotNull(
                tarea.fechaInicio?.take(10)?.let { "Inicio: $it" },
                tarea.fechaFin?.take(10)?.let { "Fin: $it" }
            ).joinToString("   ")
            if (fechas.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.CalendarToday, null, tint = TextDisabled, modifier = Modifier.size(11.dp))
                    Text(fechas, color = TextDisabled, fontSize = 11.sp)
                }
            }
        }
    }
}

// ─── Lista de sprints ─────────────────────────────────────────────────────────

@Composable
private fun SprintsList(sprints: List<ProyectoSprintDto>) {
    Surface(modifier = Modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(4.dp)) {
            sprints.forEachIndexed { index, sprint ->
                SprintItem(sprint)
                if (index < sprints.lastIndex)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun SprintItem(sprint: ProyectoSprintDto) {
    val estadoAccent = estadoColor(sprint.estado)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(estadoAccent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.AutoMirrored.Filled.Assignment, null, tint = estadoAccent, modifier = Modifier.size(18.dp)) }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(sprint.displayNombre, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)

            sprint.estado?.let { MiniChip(it.replaceFirstChar { c -> c.uppercase() }, estadoAccent) }

            if (!sprint.descripcion.isNullOrBlank()) {
                Text(sprint.descripcion, color = TextSecondary, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            val fechas = listOfNotNull(
                sprint.fechaInicio?.take(10)?.let { "Inicio: $it" },
                sprint.fechaFin?.take(10)?.let { "Fin: $it" }
            ).joinToString("   ")
            if (fechas.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.CalendarToday, null, tint = TextDisabled, modifier = Modifier.size(11.dp))
                    Text(fechas, color = TextDisabled, fontSize = 11.sp)
                }
            }
        }
    }
}

// ─── Helpers compartidos ──────────────────────────────────────────────────────

@Composable
private fun MiniChip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(color.copy(alpha = 0.12f))
            .border(0.5.dp, color.copy(alpha = 0.3f), RoundedCornerShape(5.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) { Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun EstadoBadge(estado: String?, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            estado?.replaceFirstChar { it.uppercase() } ?: "Sin estado",
            color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun StatChip(modifier: Modifier, icon: ImageVector, label: String, value: String, accent: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCard)
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, null, tint = accent, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.height(10.dp))
            Text(value, color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(label, color = TextDisabled, fontSize = 9.sp, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
private fun SectionLabel(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(icon, null, tint = AccentBlueLight, modifier = Modifier.size(14.dp))
        Text(text, color = TextDisabled, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DateRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextSecondary, fontSize = 13.sp)
        Text(value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
