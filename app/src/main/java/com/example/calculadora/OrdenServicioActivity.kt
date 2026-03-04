package com.example.calculadora

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.calculadora.databinding.ActivityOrdenServicioBinding

/**
 * Activity principal para el registro de órdenes de servicio técnico.
 * Permite capturar datos del cliente, seleccionar servicio y notificar por correo.
 */
class OrdenServicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdenServicioBinding

    // Objeto estructurado para manejar los datos de la orden
    private val ordenServicio = OrdenServicio()

    companion object {
        private const val TAG = "OrdenServicioActivity"
        private const val REQUEST_CODE_SELECCIONAR_SERVICIO = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdenServicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.titulo_orden_servicio)
        }
    }

    private fun setupListeners() {
        // Boton para seleccionar servicio - abre el catalogo
        binding.btnSeleccionarServicio.setOnClickListener {
            Log.d(TAG, "Abriendo catalogo de servicios...")
            val intent = Intent(this, CatalogoServiciosActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SELECCIONAR_SERVICIO)
        }

        // Icono de telefono - abre el marcador con el numero ingresado
        binding.iconLlamar.setOnClickListener {
            realizarLlamada()
        }

        // Boton Finalizar y Notificar - envia correo de confirmacion
        binding.btnFinalizarNotificar.setOnClickListener {
            finalizarYNotificar()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (requestCode == REQUEST_CODE_SELECCIONAR_SERVICIO) {
            if (resultCode == RESULT_OK && data != null) {
                val servicioSeleccionado = data.getStringExtra(CatalogoServiciosActivity.EXTRA_SERVICIO_SELECCIONADO)
                Log.d(TAG, "Servicio seleccionado: $servicioSeleccionado")

                if (!servicioSeleccionado.isNullOrBlank()) {
                    ordenServicio.servicioSeleccionado = servicioSeleccionado
                    binding.txtServicioSeleccionado.text = servicioSeleccionado
                    // Cambiar color a negro cuando hay servicio seleccionado
                    binding.txtServicioSeleccionado.setTextColor(Color.BLACK)
                    // Cambiar color del icono
                    binding.iconServicio.setColorFilter(
                        ContextCompat.getColor(this, R.color.design_default_color_primary)
                    )
                    Toast.makeText(this, "Servicio: $servicioSeleccionado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(TAG, "Seleccion cancelada o sin datos")
            }
        }
    }

    /**
     * Abre el marcador telefonico con el numero ingresado.
     * Usa Intent implicito con esquema tel:
     */
    private fun realizarLlamada() {
        val telefono = binding.editTelefono.text.toString().trim()

        if (telefono.isBlank()) {
            binding.editTelefono.error = getString(R.string.error_telefono_vacio)
            return
        }

        val intentLlamada = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$telefono")
        }

        try {
            startActivity(intentLlamada)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_no_marcador), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Valida los campos y envia correo de confirmacion.
     * Usa Intent implicito con esquema mailto:
     */
    private fun finalizarYNotificar() {
        // Actualizar el objeto con los datos actuales del formulario
        ordenServicio.apply {
            nombreCliente = binding.editNombreCliente.text.toString().trim()
            telefono = binding.editTelefono.text.toString().trim()
            correo = binding.editCorreo.text.toString().trim()
        }

        // Validar que todos los campos esten completos
        if (!validarCampos()) {
            return
        }

        // Crear Intent para enviar correo
        val intentCorreo = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${ordenServicio.correo}")
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.asunto_correo_confirmacion))
            putExtra(Intent.EXTRA_TEXT, ordenServicio.generarMensajeConfirmacion())
        }

        try {
            startActivity(intentCorreo)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_no_app_correo), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Valida que todos los campos obligatorios esten completos.
     * Muestra errores en los campos vacios.
     */
    private fun validarCampos(): Boolean {
        var esValido = true

        if (ordenServicio.nombreCliente.isBlank()) {
            binding.editNombreCliente.error = getString(R.string.error_campo_vacio)
            esValido = false
        }

        if (ordenServicio.telefono.isBlank()) {
            binding.editTelefono.error = getString(R.string.error_campo_vacio)
            esValido = false
        }

        if (ordenServicio.correo.isBlank()) {
            binding.editCorreo.error = getString(R.string.error_campo_vacio)
            esValido = false
        }

        if (ordenServicio.servicioSeleccionado.isBlank()) {
            Toast.makeText(this, getString(R.string.error_servicio_no_seleccionado), Toast.LENGTH_SHORT).show()
            esValido = false
        }

        return esValido
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
