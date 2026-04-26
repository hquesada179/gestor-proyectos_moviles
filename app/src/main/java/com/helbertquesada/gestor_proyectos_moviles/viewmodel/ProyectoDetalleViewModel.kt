package com.helbertquesada.gestor_proyectos_moviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.helbertquesada.gestor_proyectos_moviles.network.ProyectoDetalleDto
import com.helbertquesada.gestor_proyectos_moviles.repository.ProyectoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProyectoDetalleUiState {
    data object Loading : ProyectoDetalleUiState
    data class Success(val proyecto: ProyectoDetalleDto) : ProyectoDetalleUiState
    data class Error(val message: String) : ProyectoDetalleUiState
}

class ProyectoDetalleViewModel(private val proyectoId: Int) : ViewModel() {

    private val repository = ProyectoRepository()

    private val _uiState = MutableStateFlow<ProyectoDetalleUiState>(ProyectoDetalleUiState.Loading)
    val uiState: StateFlow<ProyectoDetalleUiState> = _uiState.asStateFlow()

    init {
        loadDetalle()
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

    companion object {
        fun factory(id: Int) = viewModelFactory {
            initializer { ProyectoDetalleViewModel(id) }
        }
    }
}
