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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Rocket
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun ProjectsScreen(onNavigate: (String) -> Unit) {
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
            ProjectsHeader()

            Spacer(Modifier.height(16.dp))

            // ── Status card ───────────────────────────────────────────────
            ProjectStatusCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(16.dp))

            // ── AI bar ────────────────────────────────────────────────────
            AiQueryBar(
                placeholder = "Buscar o preguntar sobre proyectos...",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // ── Proyectos activos ─────────────────────────────────────────
            ProjectsSectionCard(
                title = "Proyectos activos",
                badge = "2 ACTIVOS",
                badgeColor = AccentBlue,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                ProjectListItem("App Gestión Móvil", "Sprint 3 · 8 tareas")
                HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
                ProjectListItem("Portal Administrativo", "Backlog · 12 tareas")
            }

            Spacer(Modifier.height(12.dp))

            // ── Módulos ────────────────────────────────────────────────────
            ProjectsSectionCard(
                title = "Módulos recientes",
                badge = "5 TOTAL",
                badgeColor = AccentVioletLight,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                ModuleStoryItem(
                    label = "Login y Autenticación",
                    meta = "8 Story Points · Sprint 3",
                    accentColor = AccentBlue
                )
                Spacer(Modifier.height(8.dp))
                ModuleStoryItem(
                    label = "Dashboard Principal",
                    meta = "5 Story Points · Backlog",
                    accentColor = AccentVioletLight
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Equipo ────────────────────────────────────────────────────
            TeamSectionCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(20.dp))

            // ── CTA ───────────────────────────────────────────────────────
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
                "ÚLTIMA SINCRONIZACIÓN HACE 5 MIN",
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

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun ProjectsHeader() {
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
private fun ProjectsSectionCard(
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
private fun ProjectListItem(name: String, meta: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(meta, color = TextSecondary, fontSize = 11.sp)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = TextDisabled, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ModuleStoryItem(label: String, meta: String, accentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkCardElevated)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.size(3.dp, 36.dp).clip(RoundedCornerShape(2.dp)).background(accentColor))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(meta, color = TextSecondary, fontSize = 11.sp)
        }
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
                listOf("H", "Q", "M").forEachIndexed { i, initial ->
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
