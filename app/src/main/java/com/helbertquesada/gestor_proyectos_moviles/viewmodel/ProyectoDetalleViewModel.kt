package com.helbertquesada.gestor_proyectos_moviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDetalleDto
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoTareaDto
import com.helbertquesada.gestor_proyectos_moviles.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ─── Estados del detalle de proyecto ─────────────────────────────────────────

sealed interface ProyectoDetalleUiState {
    data object Loading : ProyectoDetalleUiState
    data class Success(val proyecto: ProyectoDetalleDto) : ProyectoDetalleUiState
    data class Error(val message: String) : ProyectoDetalleUiState
}

// ─── Estados de las tareas ────────────────────────────────────────────────────

sealed interface TareasUiState {
    data object Loading : TareasUiState
    data class Success(val tareas: List<ProyectoTareaDto>) : TareasUiState
    data class Error(val message: String) : TareasUiState
}

// ─── ViewModel ────────────────────────────────────────────────────────────────

class ProyectoDetalleViewModel(private val proyectoId: Int) : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow<ProyectoDetalleUiState>(ProyectoDetalleUiState.Loading)
    val uiState: StateFlow<ProyectoDetalleUiState> = _uiState.asStateFlow()

    private val _tareasUiState = MutableStateFlow<TareasUiState>(TareasUiState.Loading)
    val tareasUiState: StateFlow<TareasUiState> = _tareasUiState.asStateFlow()

    init {
        // Both requests start in parallel on creation
        loadDetalle()
        loadTareas()
    }

    fun loadDetalle() {
        viewModelScope.launch {
            _uiState.value = ProyectoDetalleUiState.Loading
            repository.getProyecto(proyectoId).fold(
                onSuccess = { _uiState.value = ProyectoDetalleUiState.Success(it) },
                onFailure = { _uiState.value = ProyectoDetalleUiState.Error(it.message ?: "Error al cargar el proyecto") }
            )
        }
    }

    fun loadTareas() {
        viewModelScope.launch {
            _tareasUiState.value = TareasUiState.Loading
            repository.getTareasByProyecto(proyectoId).fold(
                onSuccess = { _tareasUiState.value = TareasUiState.Success(it) },
                onFailure = { _tareasUiState.value = TareasUiState.Error(it.message ?: "Error al cargar las tareas") }
            )
        }
    }

    fun reload() {
        loadDetalle()
        loadTareas()
    }

    companion object {
        fun factory(id: Int) = viewModelFactory {
            initializer { ProyectoDetalleViewModel(id) }
        }
    }
}
