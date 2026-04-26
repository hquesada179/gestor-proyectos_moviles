package com.helbertquesada.gestor_proyectos_moviles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.helbertquesada.gestor_proyectos_moviles.screens.HomeScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.LoginScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.ProfileScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.ProjectsScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.RegisterScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.RequirementsScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.SprintsScreen
import com.helbertquesada.gestor_proyectos_moviles.screens.TasksScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

private fun isGoogleProvider(user: FirebaseUser): Boolean =
    user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }

private fun NavController.navigateMain(route: String) {
    navigate(route) {
        popUpTo("home") { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

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

    val onLogout: () -> Unit = {
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
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
                onClickLogout = onLogout,
                onNavigate = { route -> navController.navigateMain(route) }
            )
        }
        composable("projects") {
            ProjectsScreen(
                onNavigate = { route -> navController.navigateMain(route) }
            )
        }
        composable("tasks") {
            TasksScreen(
                onNavigate = { route -> navController.navigateMain(route) }
            )
        }
        composable("requirements") {
            RequirementsScreen(
                onNavigate = { route -> navController.navigateMain(route) }
            )
        }
        composable("sprints") {
            SprintsScreen(
                onNavigate = { route -> navController.navigateMain(route) }
            )
        }
        composable("profile") {
            ProfileScreen(
                onClickLogout = onLogout,
                onNavigate = { route -> navController.navigateMain(route) }
            )
        }
    }
}
