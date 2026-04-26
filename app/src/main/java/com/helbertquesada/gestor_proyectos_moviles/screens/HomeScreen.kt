package com.helbertquesada.gestor_proyectos_moviles.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentAmber
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlue
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlueLight
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

// ─── File-level types ─────────────────────────────────────────────────────────

private data class StatInfo(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val accent: Color,
    val delta: String
)

private data class ModuleInfo(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val accent: Color,
    val route: String
)

private data class ActivityInfo(
    val title: String,
    val subtitle: String,
    val time: String,
    val icon: ImageVector,
    val accent: Color
)

// ─── HomeScreen ───────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(onClickLogout: () -> Unit, onNavigate: (String) -> Unit) {
    val currentUser = Firebase.auth.currentUser
    val userEmail = currentUser?.email ?: "usuario@correo.com"
    val userDisplayName = currentUser?.displayName
    val userInitial = (userDisplayName?.firstOrNull() ?: userEmail.firstOrNull() ?: 'U').uppercaseChar()

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = { AppBottomBar(currentRoute = "home", onNavigate = onNavigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            HomeHeader(userInitial = userInitial)

            Column(modifier = Modifier.padding(horizontal = 16.dp).padding(top = 20.dp)) {
                Text("Hola, bienvenido", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(userEmail, color = AccentBlueLight, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(Modifier.height(16.dp))
            MainSummaryCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))
            SectionTitle("INDICADORES RÁPIDOS", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))
            QuickStatsSection(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))
            SectionTitle("MÓDULOS PRINCIPALES", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))
            ModulesSection(modifier = Modifier.padding(horizontal = 16.dp), onNavigate = onNavigate)

            Spacer(Modifier.height(20.dp))
            SectionTitle("ACTIVIDAD RECIENTE", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))
            RecentActivitySection(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(24.dp))
            OutlinedButton(
                onClick = { Firebase.auth.signOut(); onClickLogout() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun HomeHeader(userInitial: Char) {
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
            Text("Gestor de Proyectos", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Notifications, "Notificaciones", tint = TextSecondary, modifier = Modifier.size(22.dp))
            }
            Box(
                modifier = Modifier.size(34.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet))),
                contentAlignment = Alignment.Center
            ) { Text(userInitial.toString(), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

// ─── Main summary card ────────────────────────────────────────────────────────

@Composable
private fun MainSummaryCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1A1060), Color(0xFF0D1E4A))))
            .border(1.dp, AccentIndigo.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SuccessColor))
                    Text("PANEL ACTIVO", color = SuccessColor, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text("Panel de Control", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Gestiona tus proyectos, tareas y requerimientos desde un solo lugar.", color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
            }
            Spacer(Modifier.width(16.dp))
            Box(
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                    .background(AccentViolet.copy(alpha = 0.25f))
                    .border(1.dp, AccentVioletLight.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Dashboard, null, tint = AccentVioletLight, modifier = Modifier.size(26.dp)) }
        }
    }
}

// ─── Quick stats ──────────────────────────────────────────────────────────────

@Composable
private fun QuickStatsSection(modifier: Modifier = Modifier) {
    val stats = listOf(
        StatInfo("PROYECTOS ACTIVOS", "3", Icons.Filled.Folder, AccentBlue, "+1 este mes"),
        StatInfo("TAREAS PENDIENTES", "12", Icons.Filled.CheckBox, AccentAmber, "4 urgentes"),
        StatInfo("REQUERIMIENTOS", "8", Icons.AutoMirrored.Filled.Assignment, AccentVioletLight, "2 nuevos"),
        StatInfo("PROGRESO GENERAL", "65%", Icons.AutoMirrored.Filled.TrendingUp, SuccessColor, "+5% esta semana")
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(Modifier.weight(1f), stats[0]); StatCard(Modifier.weight(1f), stats[1])
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(Modifier.weight(1f), stats[2]); StatCard(Modifier.weight(1f), stats[3])
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, stat: StatInfo) {
    Surface(modifier = modifier, color = DarkCard, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(stat.accent.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(stat.icon, null, tint = stat.accent, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(stat.value, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(stat.label, color = TextSecondary, fontSize = 9.sp, letterSpacing = 0.5.sp, lineHeight = 12.sp)
            Spacer(Modifier.height(6.dp))
            Text(stat.delta, color = stat.accent, fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// ─── Modules ──────────────────────────────────────────────────────────────────

@Composable
private fun ModulesSection(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val modules = listOf(
        ModuleInfo("Proyectos", "Proyectos activos y archivados", Icons.Filled.Folder, AccentBlue, "projects"),
        ModuleInfo("Tareas", "Seguimiento de tareas del equipo", Icons.Filled.CheckBox, AccentAmber, "tasks"),
        ModuleInfo("Requerimientos", "Gestión de requerimientos", Icons.AutoMirrored.Filled.Assignment, AccentVioletLight, "requirements"),
        ModuleInfo("Sprints", "Planificación y seguimiento", Icons.Filled.Refresh, SuccessColor, "sprints")
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ModuleCard(Modifier.weight(1f), modules[0]) { onNavigate(modules[0].route) }
            ModuleCard(Modifier.weight(1f), modules[1]) { onNavigate(modules[1].route) }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ModuleCard(Modifier.weight(1f), modules[2]) { onNavigate(modules[2].route) }
            ModuleCard(Modifier.weight(1f), modules[3]) { onNavigate(modules[3].route) }
        }
    }
}

@Composable
private fun ModuleCard(modifier: Modifier = Modifier, module: ModuleInfo, onClick: () -> Unit) {
    Surface(modifier = modifier.clickable(onClick = onClick), color = DarkCardElevated, shape = RoundedCornerShape(14.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp))
                    .background(module.accent.copy(alpha = 0.18f))
                    .border(1.dp, module.accent.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(module.icon, null, tint = module.accent, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.height(12.dp))
            Text(module.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(module.description, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}

// ─── Recent activity ──────────────────────────────────────────────────────────

@Composable
private fun RecentActivitySection(modifier: Modifier = Modifier) {
    val activities = listOf(
        ActivityInfo("Tarea 'Diseño de Login' completada", "Proyecto App Móvil", "Hace 2h", Icons.Filled.CheckCircle, SuccessColor),
        ActivityInfo("Proyecto 'App Móvil' actualizado", "v1.2 – nuevo módulo auth", "Hace 5h", Icons.Filled.Folder, AccentBlue),
        ActivityInfo("Nuevo requerimiento añadido", "Auth con Google OAuth", "Hace 1d", Icons.AutoMirrored.Filled.Assignment, AccentVioletLight),
        ActivityInfo("Sprint 3 iniciado", "14 tareas planificadas", "Hace 1d", Icons.Filled.Refresh, AccentAmber)
    )
    Surface(modifier = modifier.fillMaxWidth(), color = DarkCard, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(4.dp)) {
            activities.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(item.accent.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                        Icon(item.icon, null, tint = item.accent, modifier = Modifier.size(18.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(item.subtitle, color = TextSecondary, fontSize = 11.sp)
                    }
                    Text(item.time, color = TextDisabled, fontSize = 10.sp)
                }
                if (index < activities.lastIndex) HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
            }
        }
    }
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, modifier = modifier)
}
