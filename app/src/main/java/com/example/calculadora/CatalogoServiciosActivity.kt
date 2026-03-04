package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.databinding.ActivityCatalogoServiciosBinding

/**
 * Activity para mostrar y seleccionar servicios del catalogo.
 * Devuelve el servicio seleccionado a la Activity que la invoco.
 */
class CatalogoServiciosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoServiciosBinding

    companion object {
        private const val TAG = "CatalogoServiciosAct"
        const val EXTRA_SERVICIO_SELECCIONADO = "servicio_seleccionado"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Iniciando catalogo de servicios")

        binding = ActivityCatalogoServiciosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupServicios()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.titulo_catalogo_servicios)
        }
    }

    private fun setupServicios() {
        Log.d(TAG, "setupServicios: Configurando listeners de servicios")

        // Configurar click listeners para cada servicio
        binding.cardMantenimiento.setOnClickListener {
            val servicio = getString(R.string.servicio_mantenimiento)
            Log.d(TAG, "Click en Mantenimiento: $servicio")
            seleccionarServicio(servicio)
        }

        binding.cardReparacion.setOnClickListener {
            val servicio = getString(R.string.servicio_reparacion)
            Log.d(TAG, "Click en Reparacion: $servicio")
            seleccionarServicio(servicio)
        }

        binding.cardInstalacion.setOnClickListener {
            val servicio = getString(R.string.servicio_instalacion)
            Log.d(TAG, "Click en Instalacion: $servicio")
            seleccionarServicio(servicio)
        }

        binding.cardGarantia.setOnClickListener {
            val servicio = getString(R.string.servicio_garantia)
            Log.d(TAG, "Click en Garantia: $servicio")
            seleccionarServicio(servicio)
        }
    }

    /**
     * Establece el resultado con el servicio seleccionado y cierra la Activity.
     * Implementa la comunicacion bidireccional devolviendo datos a la Activity anterior.
     */
    private fun seleccionarServicio(nombreServicio: String) {
        Log.d(TAG, "seleccionarServicio: Devolviendo servicio = $nombreServicio")

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_SERVICIO_SELECCIONADO, nombreServicio)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "onSupportNavigateUp: Cancelando seleccion")
        setResult(RESULT_CANCELED)
        finish()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: Cancelando seleccion")
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }
}
