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
    data class Success(val proyectos: List<ProyectoDto>) : ProyectoUiState
    data class Error(val message: String) : ProyectoUiState
}

class ProyectoViewModel : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow<ProyectoUiState>(ProyectoUiState.Loading)
    val uiState: StateFlow<ProyectoUiState> = _uiState.asStateFlow()

    init {
        loadProyectos()
    }

    fun loadProyectos() {
        viewModelScope.launch {
            _uiState.value = ProyectoUiState.Loading
            repository.getProyectos().fold(
                onSuccess = { _uiState.value = ProyectoUiState.Success(it) },
                onFailure = { _uiState.value = ProyectoUiState.Error(it.message ?: "Error al cargar proyectos") }
            )
        }
    }
}
