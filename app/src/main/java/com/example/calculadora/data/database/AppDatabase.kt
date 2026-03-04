package com.example.calculadora.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.calculadora.data.dao.EquipoDao
import com.example.calculadora.data.dao.LaboratorioDao
import com.example.calculadora.data.entities.Equipo
import com.example.calculadora.data.entities.Laboratorio

@Database(
    entities = [Laboratorio::class, Equipo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun laboratorioDao(): LaboratorioDao
    abstract fun equipoDao(): EquipoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "techaudit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
