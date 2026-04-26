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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Rocket
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDto
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
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.ProyectoUiState
import com.helbertquesada.gestor_proyectos_moviles.viewmodel.ProyectoViewModel
import kotlinx.coroutines.delay

private const val POLL_INTERVAL_MS = 10_000L

@Composable
fun ProjectsScreen(
    onNavigate: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit = {},
    viewModel: ProyectoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Polling loop — starts on enter, cancels automatically on leave (LaunchedEffect lifecycle)
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadProyectos()
            delay(POLL_INTERVAL_MS)
        }
    }

    // Auto-dismiss the refresh-error banner after 5 s
    val refreshError = (uiState as? ProyectoUiState.Success)?.refreshError
    LaunchedEffect(refreshError) {
        if (refreshError != null) {
            delay(5_000L)
            viewModel.clearRefreshError()
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = { AppBottomBar(currentRoute = "projects", onNavigate = onNavigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            ProjectsHeader(isPolling = uiState is ProyectoUiState.Success)

            Spacer(Modifier.height(16.dp))
            ProjectStatusCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(16.dp))
            AiQueryBar(
                placeholder = "Buscar o preguntar sobre proyectos...",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Non-blocking refresh-error banner (only when we already have data)
            AnimatedVisibility(
                visible = refreshError != null,
                enter = slideInVertically { -it } + fadeIn(),
                exit  = slideOutVertically { -it } + fadeOut()
            ) {
                RefreshErrorBanner(
                    message = refreshError ?: "",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(12.dp))
            ProyectosSection(
                uiState            = uiState,
                onRetry            = { viewModel.loadProyectos() },
                onNavigateToDetail = onNavigateToDetail,
                modifier           = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))
            TeamSectionCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(listOf(AccentIndigo, AccentViolet)))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Rocket, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("Generar Roadmap", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "SINCRONIZACIÓN AUTOMÁTICA CADA 10 SEG",
                color = TextDisabled,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ─── Refresh-error banner ─────────────────────────────────────────────────────

@Composable
private fun RefreshErrorBanner(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ErrorColor.copy(alpha = 0.08f))
            .border(1.dp, ErrorColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(16.dp))
        Text(
            text = "Sin conexión – mostrando datos anteriores",
            color = ErrorColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

// ─── Sección dinámica de proyectos ────────────────────────────────────────────

@Composable
private fun ProyectosSection(
    uiState: ProyectoUiState,
    onRetry: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ProyectoUiState.Loading -> ProyectosLoading(modifier)
        is ProyectoUiState.Error   -> ProyectosError(uiState.message, onRetry, modifier)
        // refreshError is handled by the banner above — don't show it here
        is ProyectoUiState.Success -> ProyectosSuccess(uiState.proyectos, onNavigateToDetail, modifier)
    }
}

@Composable
private fun ProyectosLoading(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(color = AccentBlue, modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
            Text("Cargando proyectos...", color = TextSecondary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun ProyectosError(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(28.dp))
            Text("No se pudo conectar al servidor", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(message, color = TextSecondary, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
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

@Composable
private fun ProyectosSuccess(
    proyectos: List<ProyectoDto>,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (proyectos.isEmpty()) {
        Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
            Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No hay proyectos registrados", color = TextSecondary, fontSize = 13.sp)
            }
        }
        return
    }

    val activos = proyectos.filter { it.estado?.lowercase() in listOf("activo", "en progreso") }
    val otros   = proyectos.filter { it !in activos }

    if (activos.isNotEmpty()) {
        ProyectosSectionCard(
            title      = "Proyectos activos",
            badge      = "${activos.size} ACTIVOS",
            badgeColor = AccentBlue,
            modifier   = modifier
        ) {
            activos.forEachIndexed { index, proyecto ->
                ProjectListItem(proyecto, onClick = { onNavigateToDetail(proyecto.id) })
                if (index < activos.lastIndex)
                    HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
            }
        }
    }

    if (otros.isNotEmpty()) {
        Spacer(Modifier.height(12.dp))
        ProyectosSectionCard(
            title      = "Otros proyectos",
            badge      = "${otros.size} TOTAL",
            badgeColor = AccentVioletLight,
            modifier   = modifier
        ) {
            otros.forEachIndexed { index, proyecto ->
                ProjectListItem(proyecto, onClick = { onNavigateToDetail(proyecto.id) })
                if (index < otros.lastIndex)
                    HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
            }
        }
    }
}

// ─── Header (con indicador de actividad de fondo) ────────────────────────────

@Composable
private fun ProjectsHeader(isPolling: Boolean) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().background(DarkBackground)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(AccentViolet),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.GridView, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
            Text("Proyectos", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            // Small live dot — visible only when polling (has data)
            if (isPolling) {
                Box(
                    modifier = Modifier.size(8.dp).clip(CircleShape).background(SuccessColor)
                )
            }
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Notifications, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
            }
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

// ─── Status card ──────────────────────────────────────────────────────────────

@Composable
private fun ProjectStatusCard(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("ESTADO ACTUAL", color = TextDisabled, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            Text("Planning Hub", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatPill(modifier = Modifier.weight(1f), label = "PROGRESO", value = "65%", accent = AccentBlue)
                StatPill(modifier = Modifier.weight(1f), label = "VELOCIDAD", value = "3.2x", accent = AccentVioletLight)
            }
        }
    }
}

@Composable
private fun StatPill(modifier: Modifier = Modifier, label: String, value: String, accent: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column {
            Text(label, color = TextDisabled, fontSize = 9.sp, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(2.dp))
            Text(value, color = accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ─── AI bar ───────────────────────────────────────────────────────────────────

@Composable
private fun AiQueryBar(placeholder: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCardElevated)
            .border(1.dp, AccentViolet.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Filled.AutoAwesome, null, tint = AccentVioletLight, modifier = Modifier.size(18.dp))
        Text(placeholder, color = TextDisabled, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(AccentIndigo),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
    }
}

// ─── Section card ─────────────────────────────────────────────────────────────

@Composable
private fun ProyectosSectionCard(
    title: String,
    badge: String,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Folder, null, tint = badgeColor, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(badgeColor.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 3.dp)
                ) { Text(badge, color = badgeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ProjectListItem(proyecto: ProyectoDto, onClick: () -> Unit) {
    val estadoDisplay = proyecto.estado?.replaceFirstChar { it.uppercase() } ?: "Sin estado"
    val meta = buildString {
        append(estadoDisplay)
        if (proyecto.sprintsCount > 0) append(" · ${proyecto.sprintsCount} sprint${if (proyecto.sprintsCount != 1) "s" else ""}")
        if (proyecto.tasksCount > 0)   append(" · ${proyecto.tasksCount} tarea${if (proyecto.tasksCount != 1) "s" else ""}")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(proyecto.nombre, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(meta, color = TextSecondary, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = TextDisabled, modifier = Modifier.size(16.dp))
    }
}

// ─── Team section ─────────────────────────────────────────────────────────────

@Composable
private fun TeamSectionCard(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Group, null, tint = AccentBlue, modifier = Modifier.size(16.dp))
                Text("Equipo", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                listOf("H", "Q", "M").forEach { initial ->
                    Box(
                        modifier = Modifier.size(32.dp).clip(CircleShape)
                            .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet)))
                            .border(2.dp, DarkCard, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text(initial, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                }
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape)
                        .background(DarkCardElevated).border(2.dp, DarkCard, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("+2", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
            }
            Spacer(Modifier.height(8.dp))
            Text("Equipo de 5 desarrolladores con roles definidos en el proyecto.", color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}
