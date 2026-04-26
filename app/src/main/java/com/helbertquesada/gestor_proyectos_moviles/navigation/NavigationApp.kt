package com.helbertquesada.gestor_proyectos_moviles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.helbertquesada.gestor_proyectos_moviles.screens.HomeScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.LoginScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.RegisterScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

private fun isGoogleProvider(user: FirebaseUser): Boolean =
    user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    val startDestination = run {
        val user = Firebase.auth.currentUser
        when {
            user == null -> "login"
            isGoogleProvider(user) -> "home"
            user.isEmailVerified -> "home"
            else -> {
                Firebase.auth.signOut()
                "login"
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onClickRegister = { navController.navigate("register") },
                onSuccessfulLogin = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onClickBack = { navController.popBackStack() },
                onSuccessfulRegister = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onClickLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
