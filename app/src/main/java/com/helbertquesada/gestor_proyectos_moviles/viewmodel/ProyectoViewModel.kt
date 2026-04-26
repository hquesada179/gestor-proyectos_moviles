package com.helbertquesada.gestor_proyectos_moviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDto
import com.helbertquesada.gestor_proyectos_moviles.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProyectoUiState {
    data object Loading : ProyectoUiState
    data class Success(
        val proyectos: List<ProyectoDto>,
        val refreshError: String? = null   // non-null = show banner, list stays visible
    ) : ProyectoUiState
    data class Error(val message: String) : ProyectoUiState
}

class ProyectoViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow<ProyectoUiState>(ProyectoUiState.Loading)
    val uiState: StateFlow<ProyectoUiState> = _uiState.asStateFlow()

    // No init block — the screen's LaunchedEffect drives the first load and all polls.

    fun loadProyectos() {
        viewModelScope.launch {
            val snapshot = _uiState.value

            // Show the full spinner only when we have no data yet
            if (snapshot !is ProyectoUiState.Success) {
                _uiState.value = ProyectoUiState.Loading
            }

            repository.getProyectos().fold(
                onSuccess = { proyectos ->
                    _uiState.value = ProyectoUiState.Success(proyectos)
                },
                onFailure = { error ->
                    _uiState.value = when (snapshot) {
                        // We already have data → keep the list, surface a small banner
                        is ProyectoUiState.Success ->
                            snapshot.copy(refreshError = error.message ?: "Sin conexión con el servidor")
                        // No data at all → show the full error screen with retry button
                        else ->
                            ProyectoUiState.Error(error.message ?: "Error al cargar proyectos")
                    }
                }
            )
        }
    }

    fun clearRefreshError() {
        val current = _uiState.value
        if (current is ProyectoUiState.Success) {
            _uiState.value = current.copy(refreshError = null)
        }
    }
}
