package com.helbertquesada.gestor_proyectos_moviles.screens

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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.ErrorColor
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.SuccessColor
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextDisabled
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextPrimary
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextSecondary

private data class TaskItem(
    val category: String,
    val categoryColor: Color,
    val title: String,
    val commentCount: Int,
    val assigneeInitial: String,
    val isUrgent: Boolean = false
)

private val taskColumns = listOf(
    "Pendiente" to listOf(
        TaskItem("FRONTEND", AccentBlue, "Implementar pantalla de Dashboard", 5, "H", false),
        TaskItem("BACKEND", AccentVioletLight, "Configurar Firebase Storage", 2, "Q", false),
        TaskItem("UX", AccentAmber, "Revisar diseño de notificaciones", 8, "M", true)
    ),
    "En Progreso" to listOf(
        TaskItem("FRONTEND", AccentBlue, "Integrar Google Sign-In en login", 12, "H", true),
        TaskItem("DEVOPS", SuccessColor, "Optimizar pipeline de CI/CD", 3, "Q", false)
    ),
    "Completado" to listOf(
        TaskItem("FRONTEND", AccentBlue, "Diseño de pantalla Login", 4, "H", false),
        TaskItem("BACKEND", AccentVioletLight, "Configurar Firebase Auth", 6, "Q", false),
        TaskItem("UX", AccentAmber, "Guía de estilos dark theme", 2, "M", false)
    )
)

@Composable
fun TasksScreen(onNavigate: (String) -> Unit) {
    var selectedColumn by remember { mutableIntStateOf(0) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            TasksHeader()
            Spacer(Modifier.height(16.dp))

            // ── Sprint info ───────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sprint 3 · Activo", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Sistema de Gestión Móvil", color = TextSecondary, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                        listOf("H", "Q", "M").forEach { i ->
                            Box(
                                modifier = Modifier.size(28.dp).clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet)))
                                    .border(2.dp, DarkBackground, CircleShape),
                                contentAlignment = Alignment.Center
                            ) { Text(i, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        }
                        Box(
                            modifier = Modifier.size(28.dp).clip(CircleShape).background(DarkCardElevated).border(2.dp, DarkBackground, CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("+4", color = TextSecondary, fontSize = 9.sp) }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Column tabs ───────────────────────────────────────────────
            val columnCounts = taskColumns.map { it.second.size }
            Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                taskColumns.forEachIndexed { index, (name, tasks) ->
                    val isSelected = selectedColumn == index
                    Column(
                        modifier = Modifier
                            .clickable { selectedColumn = index }
                            .padding(end = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                name,
                                color = if (isSelected) TextPrimary else TextDisabled,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Box(
                                modifier = Modifier.clip(CircleShape)
                                    .background(if (isSelected) AccentBlue.copy(alpha = 0.2f) else DarkCardElevated)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "${tasks.size}",
                                    color = if (isSelected) AccentBlue else TextDisabled,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier.height(2.dp).width(if (isSelected) 40.dp else 0.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(if (isSelected) AccentBlue else Color.Transparent)
                        )
                    }
                }
            }
            HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(12.dp))

            // ── Task cards ────────────────────────────────────────────────
            val currentTasks = taskColumns[selectedColumn].second
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                currentTasks.forEach { task -> TaskCard(task = task) }
            }

            Spacer(Modifier.height(20.dp))

            // ── Sprint velocity ───────────────────────────────────────────
            SprintVelocityCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun TasksHeader() {
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
            Text("Tareas", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Notifications, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
            }
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

// ─── Task card ────────────────────────────────────────────────────────────────

@Composable
private fun TaskCard(task: TaskItem) {
    Surface(color = DarkCard, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                        .background(task.categoryColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) { Text(task.category, color = task.categoryColor, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.weight(1f))
                if (task.isUrgent) {
                    Icon(Icons.Filled.Warning, null, tint = ErrorColor, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(task.title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp)
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ChatBubbleOutline, null, tint = TextDisabled, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("${task.commentCount}", color = TextDisabled, fontSize = 11.sp)
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier.size(24.dp).clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet))),
                    contentAlignment = Alignment.Center
                ) { Text(task.assigneeInitial, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

// ─── Sprint velocity ──────────────────────────────────────────────────────────

@Composable
private fun SprintVelocityCard(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("SPRINT VELOCITY", color = TextDisabled, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column {
                    Text("72%", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("Tareas completadas", color = TextSecondary, fontSize = 12.sp)
                }
                Spacer(Modifier.weight(1f))
                // Mini bar chart
                Row(
                    modifier = Modifier.height(48.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(0.4f, 0.6f, 0.5f, 0.85f, 0.72f).forEachIndexed { i, h ->
                        val isHighlight = i == 3
                        Box(
                            modifier = Modifier.width(16.dp)
                                .fillMaxSize(h)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(if (isHighlight) AccentBlue else AccentBlue.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }
    }
}
