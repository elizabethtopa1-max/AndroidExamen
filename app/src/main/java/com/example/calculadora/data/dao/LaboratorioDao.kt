package com.example.calculadora.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.calculadora.data.entities.Laboratorio

@Dao
interface LaboratorioDao {

    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLaboratorios(): LiveData<List<Laboratorio>>

    @Query("SELECT * FROM laboratorios WHERE id = :id")
    suspend fun getLaboratorioById(id: Long): Laboratorio?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(laboratorio: Laboratorio): Long

    @Update
    suspend fun update(laboratorio: Laboratorio)

    @Delete
    suspend fun delete(laboratorio: Laboratorio)

    @Query("DELETE FROM laboratorios")
    suspend fun deleteAll()
}
