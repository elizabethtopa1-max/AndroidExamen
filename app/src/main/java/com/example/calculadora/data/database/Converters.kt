package com.example.calculadora.data.database

import androidx.room.TypeConverter
import com.example.calculadora.data.entities.EstadoEquipo

class Converters {
    @TypeConverter
    fun fromEstadoEquipo(estado: EstadoEquipo): String {
        return estado.name
    }

    @TypeConverter
    fun toEstadoEquipo(estado: String): EstadoEquipo {
        return EstadoEquipo.valueOf(estado)
    }
}
