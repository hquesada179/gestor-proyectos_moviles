package com.helbertquesada.gestor_proyectos_moviles.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.helbertquesada.gestor_proyectos_moviles.network.TareaDto
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentAmber
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlue
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentIndigo
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
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.TareaUiState
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.TareaViewModel
import kotlinx.coroutines.delay

private const val TASKS_POLL_MS = 10_000L

private val ESTADO_COLUMNS = listOf("Pendiente", "En progreso", "Completado")

private fun prioridadColor(prioridad: String?): Color = when (prioridad?.lowercase()?.trim()) {
    "alta", "high", "urgente", "urgent", "critica", "crítica", "critical" -> ErrorColor
    "media", "medium", "normal" -> AccentAmber
    "baja", "low" -> AccentBlue
    else -> AccentVioletLight
}

private fun prioridadLabel(prioridad: String?): String = when (prioridad?.lowercase()?.trim()) {
    "alta", "high", "urgente", "urgent", "critica", "crítica", "critical" -> "ALTA"
    "media", "medium", "normal" -> "MEDIA"
    "baja", "low" -> "BAJA"
    else -> prioridad?.uppercase()?.take(6) ?: "—"
}

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun TasksScreen(
    onNavigate: (String) -> Unit,
    viewModel: TareaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedColumn by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadTareas()
            delay(TASKS_POLL_MS)
        }
    }

    val refreshError = (uiState as? TareaUiState.Success)?.refreshError
    LaunchedEffect(refreshError) {
        if (refreshError != null) {
            delay(5_000L)
            viewModel.clearRefreshError()
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = { AppBottomBar(currentRoute = "tasks", onNavigate = onNavigate) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = AccentBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Filled.Add, "Nueva tarea", modifier = Modifier.size(24.dp)) }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is TareaUiState.Loading -> TasksLoadingState(modifier = Modifier.padding(innerPadding))
            is TareaUiState.Error   -> TasksErrorState(
                message  = state.message,
                onRetry  = { viewModel.loadTareas() },
                modifier = Modifier.padding(innerPadding)
            )
            is TareaUiState.Success -> {
                val grouped = ESTADO_COLUMNS.associateWith { col ->
                    state.tareas.filter { it.estadoNormalizado == col }
                }
                val total      = state.tareas.size
                val completadas = grouped["Completado"]?.size ?: 0
                val enProgreso  = grouped["En progreso"]?.size ?: 0
                val pct = if (total > 0) (completadas * 100) / total else 0

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBackground)
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                ) {
                    TasksHeader()
                    Spacer(Modifier.height(16.dp))

                    // Non-blocking refresh error banner
                    AnimatedVisibility(
                        visible = refreshError != null,
                        enter = slideInVertically { -it } + fadeIn(),
                        exit  = slideOutVertically { -it } + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            color = ErrorColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(14.dp))
                                Text(refreshError ?: "", color = ErrorColor, fontSize = 12.sp)
                            }
                        }
                    }

                    // Stats chips
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskStatChip("Total", "$total",       AccentBlue,   Modifier.weight(1f))
                        TaskStatChip("Completado", "$pct%",   SuccessColor, Modifier.weight(1f))
                        TaskStatChip("En curso", "$enProgreso", AccentAmber, Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(16.dp))

                    // Column tabs
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ESTADO_COLUMNS.forEachIndexed { index, name ->
                            val isSelected = selectedColumn == index
                            val count = grouped[name]?.size ?: 0
                            Column(
                                modifier = Modifier
                                    .clickable { selectedColumn = index }
                                    .padding(end = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        name,
                                        color = if (isSelected) TextPrimary else TextDisabled,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) AccentBlue.copy(alpha = 0.2f)
                                                else DarkCardElevated
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            "$count",
                                            color = if (isSelected) AccentBlue else TextDisabled,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .height(2.dp)
                                        .width(if (isSelected) 40.dp else 0.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(if (isSelected) AccentBlue else Color.Transparent)
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        color = BorderDefault.copy(alpha = 0.4f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Task cards or empty state
                    val currentTasks = grouped[ESTADO_COLUMNS[selectedColumn]] ?: emptyList()
                    if (currentTasks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Aún no hay tareas registradas.",
                                color = TextDisabled,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            currentTasks.forEach { tarea -> RealTaskCard(tarea = tarea) }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    TasksVelocityCard(
                        total = total,
                        completadas = completadas,
                        enProgreso = enProgreso,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

// ─── Loading state ────────────────────────────────────────────────────────────

@Composable
private fun TasksLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AccentBlue, strokeWidth = 2.dp, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(12.dp))
            Text("Cargando tareas...", color = TextSecondary, fontSize = 13.sp)
        }
    }
}

// ─── Error state ──────────────────────────────────────────────────────────────

@Composable
private fun TasksErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ErrorColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("Sin conexión", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                message,
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Reintentar", fontSize = 14.sp)
            }
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun TasksHeader() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBackground)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AccentViolet),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.GridView, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
            Text(
                "Tareas",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Notifications, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
            }
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

// ─── Stat chip ────────────────────────────────────────────────────────────────

@Composable
private fun TaskStatChip(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = accentColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = accentColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(label, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// ─── Real task card ───────────────────────────────────────────────────────────

@Composable
private fun RealTaskCard(tarea: TareaDto) {
    val color = prioridadColor(tarea.prioridad)
    val label = prioridadLabel(tarea.prioridad)
    val isUrgent = tarea.prioridad?.lowercase()?.trim()
        ?.let { it == "alta" || it == "high" || it == "urgente" || it == "urgent" } == true

    val initial = tarea.displayAsignado
        ?.firstOrNull()
        ?.uppercaseChar()
        ?.toString()
        ?: "?"

    Surface(color = DarkCard, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                tarea.proyectoNombre?.takeIf { it.isNotBlank() }?.let { pn ->
                    Text(
                        pn,
                        color = TextDisabled,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                } ?: Spacer(Modifier.weight(1f))
                if (isUrgent) {
                    Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                tarea.displayTitle,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            tarea.descripcion?.takeIf { it.isNotBlank() }?.let { desc ->
                Spacer(Modifier.height(4.dp))
                Text(
                    desc,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                tarea.fechaFin?.takeIf { it.isNotBlank() }?.let { fecha ->
                    Text(
                        fecha.take(10),
                        color = TextDisabled,
                        fontSize = 11.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet)))
                        .border(1.5.dp, DarkCard, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(initial, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─── Velocity card (computed from real data) ──────────────────────────────────

@Composable
private fun TasksVelocityCard(
    total: Int,
    completadas: Int,
    enProgreso: Int,
    modifier: Modifier = Modifier
) {
    val pct = if (total > 0) (completadas * 100) / total else 0
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "RESUMEN DE TAREAS",
                color = TextDisabled,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text("$pct%", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("Tareas completadas", color = TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$enProgreso en progreso · ${total - completadas - enProgreso} pendientes",
                        color = TextDisabled,
                        fontSize = 11.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                // Visual bars proportional to real data
                Row(
                    modifier = Modifier.height(48.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val pendientes = total - completadas - enProgreso
                    val barData = listOf(
                        (if (total > 0) pendientes.toFloat() / total else 0f) to false,
                        (if (total > 0) enProgreso.toFloat() / total else 0f) to false,
                        (if (total > 0) completadas.toFloat() / total else 0f) to true
                    )
                    barData.forEach { (h, highlight) ->
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .fillMaxSize(h.coerceAtLeast(0.05f))
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(
                                    if (highlight) AccentBlue
                                    else AccentBlue.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }
        }
    }
}

