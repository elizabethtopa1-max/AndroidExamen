package com.example.calculadora.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.calculadora.data.entities.Equipo

@Dao
interface EquipoDao {

    @Query("SELECT * FROM equipos WHERE laboratorioId = :laboratorioId ORDER BY nombre ASC")
    fun getEquiposByLaboratorio(laboratorioId: Long): LiveData<List<Equipo>>

    @Query("SELECT * FROM equipos")
    suspend fun getAllEquipos(): List<Equipo>

    @Query("SELECT * FROM equipos WHERE id = :id")
    suspend fun getEquipoById(id: Long): Equipo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equipo: Equipo): Long

    @Update
    suspend fun update(equipo: Equipo)

    @Delete
    suspend fun delete(equipo: Equipo)

    @Query("DELETE FROM equipos WHERE laboratorioId = :laboratorioId")
    suspend fun deleteByLaboratorio(laboratorioId: Long)
}
