package com.helbertquesada.gestor_proyectos_moviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helbertquesada.gestor_proyectos_moviles.network.TareaDto
import com.helbertquesada.gestor_proyectos_moviles.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TareaUiState {
    data object Loading : TareaUiState
    data class Success(
        val tareas: List<TareaDto>,
        val refreshError: String? = null
    ) : TareaUiState
    data class Error(val message: String) : TareaUiState
}

class TareaViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow<TareaUiState>(TareaUiState.Loading)
    val uiState: StateFlow<TareaUiState> = _uiState.asStateFlow()

    fun loadTareas() {
        viewModelScope.launch {
            val snapshot = _uiState.value

            if (snapshot !is TareaUiState.Success) {
                _uiState.value = TareaUiState.Loading
            }

            repository.getTareas().fold(
                onSuccess = { tareas ->
                    _uiState.value = TareaUiState.Success(tareas)
                },
                onFailure = { error ->
                    _uiState.value = when (snapshot) {
                        is TareaUiState.Success ->
                            snapshot.copy(refreshError = error.message ?: "Sin conexión con el servidor")
                        else ->
                            TareaUiState.Error(error.message ?: "Error al cargar tareas")
                    }
                }
            )
        }
    }

    fun clearRefreshError() {
        val current = _uiState.value
        if (current is TareaUiState.Success) {
            _uiState.value = current.copy(refreshError = null)
        }
    }
}
