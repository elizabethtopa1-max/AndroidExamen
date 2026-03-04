package com.example.calculadora.data.repository

import androidx.lifecycle.LiveData
import com.example.calculadora.data.api.RetrofitInstance
import com.example.calculadora.data.dao.EquipoDao
import com.example.calculadora.data.dao.LaboratorioDao
import com.example.calculadora.data.entities.Equipo
import com.example.calculadora.data.entities.Laboratorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TechAuditRepository(
    private val laboratorioDao: LaboratorioDao,
    private val equipoDao: EquipoDao
) {

    val allLaboratorios: LiveData<List<Laboratorio>> = laboratorioDao.getAllLaboratorios()

    fun getEquiposByLaboratorio(laboratorioId: Long): LiveData<List<Equipo>> {
        return equipoDao.getEquiposByLaboratorio(laboratorioId)
    }

    suspend fun insertLaboratorio(laboratorio: Laboratorio): Long {
        return withContext(Dispatchers.IO) {
            laboratorioDao.insert(laboratorio)
        }
    }

    suspend fun updateLaboratorio(laboratorio: Laboratorio) {
        withContext(Dispatchers.IO) {
            laboratorioDao.update(laboratorio)
        }
    }

    suspend fun deleteLaboratorio(laboratorio: Laboratorio) {
        withContext(Dispatchers.IO) {
            laboratorioDao.delete(laboratorio)
        }
    }

    suspend fun insertEquipo(equipo: Equipo): Long {
        return withContext(Dispatchers.IO) {
            equipoDao.insert(equipo)
        }
    }

    suspend fun updateEquipo(equipo: Equipo) {
        withContext(Dispatchers.IO) {
            equipoDao.update(equipo)
        }
    }

    suspend fun deleteEquipo(equipo: Equipo) {
        withContext(Dispatchers.IO) {
            equipoDao.delete(equipo)
        }
    }

    suspend fun sincronizarConNube(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val laboratorios = laboratorioDao.getAllLaboratorios().value ?: emptyList()
                val equipos = equipoDao.getAllEquipos()

                var laboratoriosEnviados = 0
                var equiposEnviados = 0

                laboratorios.forEach { laboratorio ->
                    val response = RetrofitInstance.api.postLaboratorio(laboratorio)
                    if (response.isSuccessful) {
                        laboratoriosEnviados++
                    }
                }

                equipos.forEach { equipo ->
                    val response = RetrofitInstance.api.postEquipo(equipo)
                    if (response.isSuccessful) {
                        equiposEnviados++
                    }
                }

                Result.success("Sincronización exitosa: $laboratoriosEnviados laboratorios y $equiposEnviados equipos enviados")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
