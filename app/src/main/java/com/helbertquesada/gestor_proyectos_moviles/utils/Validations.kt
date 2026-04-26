package com.helbertquesada.gestor_proyectos_moviles.utils

fun validateEmail(email: String): Pair<Boolean, String> {
    if (email.isBlank()) return Pair(false, "El correo es requerido")
    val pattern = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
    if (!pattern.matches(email.trim())) return Pair(false, "Ingresa un correo válido")
    return Pair(true, "")
}

fun validatePassword(password: String): Pair<Boolean, String> {
    if (password.isBlank()) return Pair(false, "La contraseña es requerida")
    if (password.length < 8) return Pair(false, "Mínimo 8 caracteres")
    return Pair(true, "")
}

fun validateName(name: String): Pair<Boolean, String> {
    if (name.isBlank()) return Pair(false, "El nombre es requerido")
    if (name.trim().length < 3) return Pair(false, "Mínimo 3 caracteres")
    return Pair(true, "")
}

fun validateConfirmPassword(password: String, confirmPassword: String): Pair<Boolean, String> {
    if (confirmPassword.isBlank()) return Pair(false, "Confirma tu contraseña")
    if (password != confirmPassword) return Pair(false, "Las contraseñas no coinciden")
    return Pair(true, "")
}
