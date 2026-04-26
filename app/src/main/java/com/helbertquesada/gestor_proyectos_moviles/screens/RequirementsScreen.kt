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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentAmber
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlue
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

private data class Requirement(
    val code: String,
    val title: String,
    val type: String,
    val typeColor: Color,
    val priority: String,
    val priorityColor: Color,
    val status: String,
    val statusColor: Color
)

private val sampleRequirements = listOf(
    Requirement("REQ-001", "Autenticación con correo y contraseña", "FUNCIONAL", AccentBlue, "ALTA", ErrorColor, "Completado", SuccessColor),
    Requirement("REQ-002", "Inicio de sesión con Google OAuth", "FUNCIONAL", AccentBlue, "ALTA", ErrorColor, "Completado", SuccessColor),
    Requirement("REQ-003", "Recuperación de contraseña por correo", "FUNCIONAL", AccentBlue, "MEDIA", AccentAmber, "Completado", SuccessColor),
    Requirement("REQ-004", "Dashboard principal con indicadores", "FUNCIONAL", AccentBlue, "ALTA", ErrorColor, "En progreso", AccentAmber),
    Requirement("REQ-005", "Gestión de proyectos y módulos", "FUNCIONAL", AccentBlue, "ALTA", ErrorColor, "En progreso", AccentAmber),
    Requirement("REQ-006", "Tablero Kanban de tareas", "FUNCIONAL", AccentBlue, "MEDIA", AccentAmber, "Pendiente", AccentVioletLight),
    Requirement("REQ-007", "Planificación y seguimiento de sprints", "FUNCIONAL", AccentBlue, "MEDIA", AccentAmber, "Pendiente", AccentVioletLight),
    Requirement("REQ-008", "Modo oscuro obligatorio", "NO FUNCIONAL", AccentVioletLight, "BAJA", SuccessColor, "Completado", SuccessColor)
)

@Composable
fun RequirementsScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        containerColor = DarkBackground,
        bottomBar = { AppBottomBar(currentRoute = "requirements", onNavigate = onNavigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            RequirementsHeader()

            Spacer(Modifier.height(16.dp))
            RequirementsOverviewCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("LISTADO DE REQUERIMIENTOS", color = TextSecondary, fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                        .background(AccentVioletLight.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("${sampleRequirements.size} TOTAL", color = AccentVioletLight, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(10.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                color = DarkCard,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    sampleRequirements.forEachIndexed { index, req ->
                        RequirementItem(req)
                        if (index < sampleRequirements.lastIndex)
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RequirementsHeader() {
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
            Text("Requerimientos", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Notifications, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
            }
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

@Composable
private fun RequirementsOverviewCard(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("RESUMEN", color = TextDisabled, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            Text("Estado del backlog", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ReqStatPill(Modifier.weight(1f), "COMPLETADOS", "3", SuccessColor)
                ReqStatPill(Modifier.weight(1f), "EN PROGRESO", "2", AccentAmber)
                ReqStatPill(Modifier.weight(1f), "PENDIENTES", "3", AccentVioletLight)
            }
        }
    }
}

@Composable
private fun ReqStatPill(modifier: Modifier = Modifier, label: String, value: String, accent: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(value, color = accent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(label, color = TextDisabled, fontSize = 8.sp, letterSpacing = 0.3.sp)
        }
    }
}

@Composable
private fun RequirementItem(req: Requirement) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(AccentVioletLight.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.AutoMirrored.Filled.Assignment, null, tint = AccentVioletLight, modifier = Modifier.size(18.dp)) }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(req.code, color = TextDisabled, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                        .background(req.typeColor.copy(alpha = 0.12f))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) { Text(req.type, color = req.typeColor, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
            }
            Text(req.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium, lineHeight = 17.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PriorityChip("P: ${req.priority}", req.priorityColor)
                PriorityChip(req.status, req.statusColor)
            }
        }

        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = TextDisabled, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun PriorityChip(label: String, color: Color) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .border(0.5.dp, color.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) { Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
}
