package com.example.calculadora.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.calculadora.data.database.AppDatabase
import com.example.calculadora.data.entities.Equipo
import com.example.calculadora.data.entities.Laboratorio
import com.example.calculadora.data.repository.TechAuditRepository
import kotlinx.coroutines.launch

class TechAuditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TechAuditRepository
    val allLaboratorios: LiveData<List<Laboratorio>>

    private val _sincronizacionEstado = MutableLiveData<SincronizacionEstado>()
    val sincronizacionEstado: LiveData<SincronizacionEstado> = _sincronizacionEstado

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TechAuditRepository(database.laboratorioDao(), database.equipoDao())
        allLaboratorios = repository.allLaboratorios
    }

    fun getEquiposByLaboratorio(laboratorioId: Long): LiveData<List<Equipo>> {
        return repository.getEquiposByLaboratorio(laboratorioId)
    }

    fun insertLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.insertLaboratorio(laboratorio)
    }

    fun updateLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.updateLaboratorio(laboratorio)
    }

    fun deleteLaboratorio(laboratorio: Laboratorio) = viewModelScope.launch {
        repository.deleteLaboratorio(laboratorio)
    }

    fun insertEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.insertEquipo(equipo)
    }

    fun updateEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.updateEquipo(equipo)
    }

    fun deleteEquipo(equipo: Equipo) = viewModelScope.launch {
        repository.deleteEquipo(equipo)
    }

    fun sincronizarConNube() = viewModelScope.launch {
        _sincronizacionEstado.value = SincronizacionEstado.Loading
        val resultado = repository.sincronizarConNube()
        _sincronizacionEstado.value = if (resultado.isSuccess) {
            SincronizacionEstado.Success(resultado.getOrNull() ?: "Sincronización exitosa")
        } else {
            SincronizacionEstado.Error(resultado.exceptionOrNull()?.message ?: "Error desconocido")
        }
    }
}

sealed class SincronizacionEstado {
    object Idle : SincronizacionEstado()
    object Loading : SincronizacionEstado()
    data class Success(val message: String) : SincronizacionEstado()
    data class Error(val message: String) : SincronizacionEstado()
}
