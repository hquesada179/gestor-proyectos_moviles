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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentBlue
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentIndigo
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentViolet
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.AccentVioletLight
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkBackground
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.DarkCard
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.ErrorColor
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.GoogleButtonBg
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.GoogleButtonBorder
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextDisabled
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextHint
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextPrimary
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.TextSecondary
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.appTextFieldColors
import com.helbertquesada.gestor_proyectos_moviles.utils.GoogleSignInResult
import com.helbertquesada.gestor_proyectos_moviles.utils.signInWithGoogle
import com.helbertquesada.gestor_proyectos_moviles.utils.validateConfirmPassword
import com.helbertquesada.gestor_proyectos_moviles.utils.validateEmail
import com.helbertquesada.gestor_proyectos_moviles.utils.validateName
import com.helbertquesada.gestor_proyectos_moviles.utils.validatePassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity
import androidx.compose.ui.platform.LocalView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth

// ─── Helpers privados para este archivo ──────────────────────────────────────

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp
    )
}

@Composable
private fun FieldError(message: String) {
    if (message.isNotEmpty()) {
        Text(
            text = message,
            color = ErrorColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

// ─── Pantalla principal ───────────────────────────────────────────────────────

@Composable
fun RegisterScreen(
    onClickBack: () -> Unit,
    onSuccessfulRegister: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var generalError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var verificationMessage by remember { mutableStateOf("") }
    val auth = Firebase.auth
    val activity = LocalView.current.context as Activity

    fun attemptRegister() {
        focusManager.clearFocus()
        keyboardController?.hide()
        val (nameOk, nameMsg) = validateName(name)
        val (emailOk, emailMsg) = validateEmail(email)
        val (passOk, passMsg) = validatePassword(password)
        val (confirmOk, confirmMsg) = validateConfirmPassword(password, confirmPassword)
        nameError = if (!nameOk) nameMsg else ""
        emailError = if (!emailOk) emailMsg else ""
        passwordError = if (!passOk) passMsg else ""
        confirmPasswordError = if (!confirmOk) confirmMsg else ""
        if (!nameOk || !emailOk || !passOk || !confirmOk) return
        generalError = ""
        isLoading = true
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verifyTask ->
                            isLoading = false
                            Firebase.auth.signOut()
                            if (verifyTask.isSuccessful) {
                                verificationMessage = "Te enviamos un correo de verificación. Revisa tu bandeja de entrada."
                                scope.launch {
                                    delay(2500)
                                    onClickBack()
                                }
                            } else {
                                generalError = "No fue posible enviar el correo de verificación."
                            }
                        }
                } else {
                    isLoading = false
                    generalError = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "El correo no tiene un formato válido"
                        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta registrada con este correo"
                        else -> "No fue posible completar el registro"
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Barra superior ────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClickBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextPrimary
                    )
                }
                Text(
                    text = "Gestor de Proyectos",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Soporte",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Logo + nombre de app ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AccentViolet),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.GridView,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Gestor de Proyectos",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Card principal con loading overlay ────────────────────────
            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkCard,
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Crear cuenta",
                                color = TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Regístrate para comenzar a gestionar tus proyectos",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }

                        // Campo nombre
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FieldLabel("NOMBRE COMPLETO")
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it; nameError = "" },
                                placeholder = { Text("Ej. Juan Pérez", color = TextHint) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Person, null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (nameError.isNotEmpty())
                                        Icon(Icons.Filled.Error, null, tint = ErrorColor, modifier = Modifier.size(18.dp))
                                },
                                isError = nameError.isNotEmpty(),
                                colors = appTextFieldColors(),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                ),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            FieldError(nameError)
                        }

                        // Campo correo
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FieldLabel("CORREO ELECTRÓNICO")
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it; emailError = "" },
                                placeholder = { Text("email@ejemplo.com", color = TextHint) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Email, null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (emailError.isNotEmpty())
                                        Icon(Icons.Filled.Error, null, tint = ErrorColor, modifier = Modifier.size(18.dp))
                                },
                                isError = emailError.isNotEmpty(),
                                colors = appTextFieldColors(),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    capitalization = KeyboardCapitalization.None,
                                    autoCorrectEnabled = false,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                ),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            FieldError(emailError)
                        }

                        // Campo contraseña
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FieldLabel("CONTRASEÑA")
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it; passwordError = "" },
                                placeholder = { Text("••••••••", color = TextHint) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Lock, null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (passwordError.isNotEmpty()) {
                                        Icon(Icons.Filled.Error, null, tint = ErrorColor, modifier = Modifier.size(18.dp))
                                    } else {
                                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                            Icon(
                                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                                contentDescription = null,
                                                tint = TextSecondary
                                            )
                                        }
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                isError = passwordError.isNotEmpty(),
                                colors = appTextFieldColors(),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    capitalization = KeyboardCapitalization.None,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                ),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            FieldError(passwordError)
                        }

                        // Campo confirmar contraseña
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FieldLabel("CONFIRMAR CONTRASEÑA")
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it; confirmPasswordError = "" },
                                placeholder = { Text("••••••••", color = TextHint) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Lock, null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (confirmPasswordError.isNotEmpty()) {
                                        Icon(Icons.Filled.Error, null, tint = ErrorColor, modifier = Modifier.size(18.dp))
                                    } else {
                                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                            Icon(
                                                imageVector = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                                contentDescription = null,
                                                tint = TextSecondary
                                            )
                                        }
                                    }
                                },
                                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                isError = confirmPasswordError.isNotEmpty(),
                                colors = appTextFieldColors(),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    capitalization = KeyboardCapitalization.None,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { attemptRegister() }),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            FieldError(confirmPasswordError)
                        }

                        // Banner de error general
                        if (generalError.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ErrorColor.copy(alpha = 0.12f))
                                    .border(1.dp, ErrorColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Warning, null,
                                    tint = ErrorColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = generalError,
                                    color = ErrorColor,
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Banner de verificación enviada
                        if (verificationMessage.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AccentBlue.copy(alpha = 0.10f))
                                    .border(1.dp, AccentBlue.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle, null,
                                    tint = AccentBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = verificationMessage,
                                    color = AccentBlue,
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Botón registro con gradiente
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(listOf(AccentIndigo, AccentViolet))
                                )
                                .clickable(enabled = !isLoading) { attemptRegister() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "REGISTRARME",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = 1.sp
                                )
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward, null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Link a login
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("¿Ya tienes cuenta? ", color = TextSecondary, fontSize = 14.sp)
                            Text(
                                text = "Inicia sesión",
                                color = AccentBlue,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onClickBack() }
                            )
                        }
                    }
                }

                // Overlay de carga
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(DarkCard.copy(alpha = 0.95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(52.dp),
                                color = AccentVioletLight,
                                strokeWidth = 3.dp
                            )
                            Text(
                                text = "Creando cuenta",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Configurando tu perfil...",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Botón Google (fuera de la card) ───────────────────────────
            OutlinedButton(
                onClick = {
                    scope.launch {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        isLoading = true
                        when (val result = signInWithGoogle(context)) {
                            is GoogleSignInResult.Success -> onSuccessfulRegister()
                            is GoogleSignInResult.Cancelled -> isLoading = false
                            is GoogleSignInResult.Error -> {
                                isLoading = false
                                generalError = result.message
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, GoogleButtonBorder),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = GoogleButtonBg)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("G", color = Color(0xFF4285F4), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "Continuar con Google",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Footer ────────────────────────────────────────────────────
            Text(
                text = "AL REGISTRARTE, ACEPTAS NUESTROS TÉRMINOS DE SERVICIO\nY POLÍTICAS DE PRIVACIDAD.",
                color = TextDisabled,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 0.3.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "© 2024 Gestor de Proyectos. Sistema de gestión empresarial.",
                color = TextDisabled,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Privacidad", color = TextDisabled, fontSize = 11.sp, modifier = Modifier.clickable { })
                Text("Términos", color = TextDisabled, fontSize = 11.sp, modifier = Modifier.clickable { })
                Text("Seguridad", color = TextDisabled, fontSize = 11.sp, modifier = Modifier.clickable { })
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
