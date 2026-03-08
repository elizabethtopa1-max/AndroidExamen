package com.example.calculadora.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laboratorios")
data class Laboratorio(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val edificio: String
)
