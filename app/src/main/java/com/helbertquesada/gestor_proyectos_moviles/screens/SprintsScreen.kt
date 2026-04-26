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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentAmber
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlue
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentIndigo
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentViolet
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentVioletLight
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.BorderDefault
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkBackground
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkCard
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkCardElevated
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.SuccessColor
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextDisabled
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextPrimary
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextSecondary

private data class SprintInfo(
    val number: Int,
    val name: String,
    val dateRange: String,
    val totalTasks: Int,
    val completedTasks: Int,
    val status: String,
    val statusColor: Color
)

private val sprintHistory = listOf(
    SprintInfo(3, "Módulos Principales", "15 Abr – 28 Abr 2026", 14, 8, "ACTIVO", AccentBlue),
    SprintInfo(2, "Autenticación", "1 Abr – 14 Abr 2026", 10, 10, "COMPLETADO", SuccessColor),
    SprintInfo(1, "Configuración Base", "18 Mar – 31 Mar 2026", 8, 8, "COMPLETADO", SuccessColor)
)

@Composable
fun SprintsScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        containerColor = DarkBackground,
        bottomBar = { AppBottomBar(currentRoute = "sprints", onNavigate = onNavigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            SprintsHeader()

            Spacer(Modifier.height(16.dp))
            ActiveSprintCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))
            Text(
                "HISTORIAL DE SPRINTS",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                color = DarkCard,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    sprintHistory.forEachIndexed { index, sprint ->
                        SprintHistoryItem(sprint)
                        if (index < sprintHistory.lastIndex)
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            SprintVelocityOverview(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SprintsHeader() {
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
            Text("Sprints", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Notifications, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
            }
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

@Composable
private fun ActiveSprintCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF0D2040), Color(0xFF1A1060))))
            .border(1.dp, AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(SuccessColor))
                Text("SPRINT ACTIVO", color = SuccessColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                        .background(AccentBlue.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) { Text("SPRINT 3", color = AccentBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(10.dp))
            Text("Módulos Principales", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("15 Abr – 28 Abr 2026", color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SprintMiniStat(Modifier.weight(1f), "COMPLETADAS", "8", AccentBlue)
                SprintMiniStat(Modifier.weight(1f), "EN PROGRESO", "3", AccentAmber)
                SprintMiniStat(Modifier.weight(1f), "PENDIENTES", "3", AccentVioletLight)
            }

            Spacer(Modifier.height(16.dp))
            Text("PROGRESO GENERAL", color = TextDisabled, fontSize = 9.sp, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { 0.57f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = AccentBlue,
                trackColor = DarkCardElevated,
                strokeCap = StrokeCap.Round
            )
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("8 de 14 tareas", color = TextSecondary, fontSize = 11.sp)
                Text("57%", color = AccentBlue, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SprintMiniStat(modifier: Modifier = Modifier, label: String, value: String, accent: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(value, color = accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(label, color = TextDisabled, fontSize = 8.sp, letterSpacing = 0.3.sp)
        }
    }
}

@Composable
private fun SprintHistoryItem(sprint: SprintInfo) {
    val progress = sprint.completedTasks.toFloat() / sprint.totalTasks
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                .background(sprint.statusColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            if (sprint.status == "COMPLETADO")
                Icon(Icons.Filled.CheckCircle, null, tint = sprint.statusColor, modifier = Modifier.size(20.dp))
            else
                Icon(Icons.Filled.Refresh, null, tint = sprint.statusColor, modifier = Modifier.size(20.dp))
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Sprint ${sprint.number}", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                        .background(sprint.statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) { Text(sprint.status, color = sprint.statusColor, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
            }
            Text(sprint.name, color = TextSecondary, fontSize = 12.sp)
            Text(sprint.dateRange, color = TextDisabled, fontSize = 11.sp)
            Spacer(Modifier.height(2.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = sprint.statusColor,
                trackColor = DarkCardElevated,
                strokeCap = StrokeCap.Round
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text("${sprint.completedTasks}/${sprint.totalTasks}", color = sprint.statusColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("tareas", color = TextDisabled, fontSize = 9.sp)
        }
    }
}

@Composable
private fun SprintVelocityOverview(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Refresh, null, tint = AccentVioletLight, modifier = Modifier.size(16.dp))
                Text("VELOCIDAD POR SPRINT", color = TextDisabled, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    Triple("S1", 1f, SuccessColor),
                    Triple("S2", 1f, SuccessColor),
                    Triple("S3", 0.57f, AccentBlue)
                ).forEach { (label, h, color) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.width(48.dp)
                    ) {
                        Box(
                            modifier = Modifier.width(36.dp).fillMaxSize(h)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(Brush.verticalGradient(listOf(color, color.copy(alpha = 0.4f))))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(label, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Promedio completado", color = TextSecondary, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("85%", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("por sprint", color = TextDisabled, fontSize = 11.sp, modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet))),
                    contentAlignment = Alignment.Center
                ) {
                    Text("3x", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
