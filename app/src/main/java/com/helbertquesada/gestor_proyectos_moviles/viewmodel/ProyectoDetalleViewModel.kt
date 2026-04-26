package com.helbertquesada.gestor_proyectos_moviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDetalleDto
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoSprintDto
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoTareaDto
import com.helbertquesada.gestor_proyectos_moviles.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ─── Estado del detalle de proyecto ──────────────────────────────────────────

sealed interface ProyectoDetalleUiState {
    data object Loading : ProyectoDetalleUiState
    data class Success(val proyecto: ProyectoDetalleDto) : ProyectoDetalleUiState
    data class Error(val message: String) : ProyectoDetalleUiState
}

// ─── Estado de las tareas ─────────────────────────────────────────────────────

sealed interface TareasUiState {
    data object Loading : TareasUiState
    data class Success(val tareas: List<ProyectoTareaDto>) : TareasUiState
    data class Error(val message: String) : TareasUiState
}

// ─── Estado de los sprints ────────────────────────────────────────────────────

sealed interface SprintsUiState {
    data object Loading : SprintsUiState
    data class Success(val sprints: List<ProyectoSprintDto>) : SprintsUiState
    data class Error(val message: String) : SprintsUiState
}

// ─── ViewModel ────────────────────────────────────────────────────────────────

class ProyectoDetalleViewModel(private val proyectoId: Int) : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow<ProyectoDetalleUiState>(ProyectoDetalleUiState.Loading)
    val uiState: StateFlow<ProyectoDetalleUiState> = _uiState.asStateFlow()

    private val _tareasUiState = MutableStateFlow<TareasUiState>(TareasUiState.Loading)
    val tareasUiState: StateFlow<TareasUiState> = _tareasUiState.asStateFlow()

    private val _sprintsUiState = MutableStateFlow<SprintsUiState>(SprintsUiState.Loading)
    val sprintsUiState: StateFlow<SprintsUiState> = _sprintsUiState.asStateFlow()

    init {
        // Three requests start in parallel on creation
        loadDetalle()
        loadTareas()
        loadSprints()
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

    fun loadSprints() {
        viewModelScope.launch {
            _sprintsUiState.value = SprintsUiState.Loading
            repository.getSprintsByProyecto(proyectoId).fold(
                onSuccess = { _sprintsUiState.value = SprintsUiState.Success(it) },
                onFailure = { _sprintsUiState.value = SprintsUiState.Error(it.message ?: "Error al cargar los sprints") }
            )
        }
    }

    fun reload() {
        loadDetalle()
        loadTareas()
        loadSprints()
    }

    companion object {
        fun factory(id: Int) = viewModelFactory {
            initializer { ProyectoDetalleViewModel(id) }
        }
    }
}
