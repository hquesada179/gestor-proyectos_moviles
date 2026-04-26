package com.helbertquesada.gestor_proyectos_moviles.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
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

@Composable
fun ProfileScreen(onClickLogout: () -> Unit, onNavigate: (String) -> Unit) {
    val user = Firebase.auth.currentUser
    val userEmail = user?.email ?: "usuario@correo.com"
    val displayName = user?.displayName
    val isGoogle = user?.providerData?.any { it.providerId == GoogleAuthProvider.PROVIDER_ID } == true
    val providerLabel = if (isGoogle) "Google" else "Correo electrónico"
    val providerColor = if (isGoogle) AccentBlue else AccentVioletLight
    val isVerified = isGoogle || (user?.isEmailVerified == true)
    val userInitial = (displayName?.firstOrNull() ?: userEmail.firstOrNull() ?: 'U').uppercaseChar()

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = { AppBottomBar(currentRoute = "profile", onNavigate = onNavigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            ProfileHeader()

            Spacer(Modifier.height(24.dp))

            // ── Avatar + name ─────────────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AccentIndigo, AccentViolet)))
                        .border(3.dp, AccentVioletLight.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(userInitial.toString(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                if (!displayName.isNullOrBlank()) {
                    Text(displayName, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(2.dp))
                }
                Text(userEmail, color = AccentBlueLight, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp))
                            .background(providerColor.copy(alpha = 0.15f))
                            .border(1.dp, providerColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) { Text("Proveedor: $providerLabel", color = providerColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    if (isVerified) {
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(6.dp))
                                .background(SuccessColor.copy(alpha = 0.12f))
                                .border(1.dp, SuccessColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) { Text("Verificado", color = SuccessColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Account info card ─────────────────────────────────────────
            Text(
                "INFORMACIÓN DE CUENTA",
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
                    ProfileInfoRow(icon = Icons.Filled.Email, label = "Correo electrónico", value = userEmail)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
                    ProfileInfoRow(icon = Icons.Filled.Person, label = "Proveedor de acceso", value = providerLabel)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
                    ProfileInfoRow(
                        icon = Icons.Filled.Shield,
                        label = "Estado de cuenta",
                        value = if (isVerified) "Verificada" else "Sin verificar",
                        valueColor = if (isVerified) SuccessColor else ErrorColor
                    )
                    if (!isGoogle) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
                        ProfileInfoRow(icon = Icons.Filled.Lock, label = "Contraseña", value = "••••••••")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── App info card ─────────────────────────────────────────────
            Text(
                "INFORMACIÓN DE LA APP",
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
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppInfoRow("Versión", "1.0.0-alpha")
                    AppInfoRow("Plataforma", "Android")
                    AppInfoRow("Base de datos", "Firebase Auth")
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Logout ────────────────────────────────────────────────────
            OutlinedButton(
                onClick = { Firebase.auth.signOut(); onClickLogout() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeader() {
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
            Text("Perfil", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        HorizontalDivider(color = BorderDefault.copy(alpha = 0.4f), thickness = 0.5.dp)
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(DarkCardElevated),
            contentAlignment = Alignment.Center
        ) { Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = TextDisabled, fontSize = 11.sp)
            Text(value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun AppInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = TextSecondary, fontSize = 13.sp)
        Text(value, color = TextDisabled, fontSize = 13.sp)
    }
}
