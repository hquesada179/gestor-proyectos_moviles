package com.helbertquesada.gestor_proyectos_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.helbertquesada.gestor_proyectos_moviles.navigation.NavigationApp
import com.helbertquesada.gestor_proyectos_moviles.ui.theme.Gestorproyectos_movilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Gestorproyectos_movilesTheme {
                NavigationApp()
            }
        }
    }
}
