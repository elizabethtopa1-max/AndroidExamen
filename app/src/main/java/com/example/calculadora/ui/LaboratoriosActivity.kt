package com.example.calculadora.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadora.R
import com.example.calculadora.data.entities.Laboratorio
import com.example.calculadora.ui.adapter.LaboratorioAdapter
import com.example.calculadora.viewmodel.SincronizacionEstado
import com.example.calculadora.viewmodel.TechAuditViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class LaboratoriosActivity : AppCompatActivity() {

    private lateinit var viewModel: TechAuditViewModel
    private lateinit var adapter: LaboratorioAdapter
    private lateinit var rvLaboratorios: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var btnSincronizar: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboratorios)

        viewModel = ViewModelProvider(this).get(TechAuditViewModel::class.java)

        setupViews()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupViews() {
        rvLaboratorios = findViewById(R.id.rvLaboratorios)
        fabAgregar = findViewById(R.id.fabAgregarLaboratorio)
        btnSincronizar = findViewById(R.id.btnSincronizar)
        progressBar = findViewById(R.id.progressBar)

        fabAgregar.setOnClickListener {
            mostrarDialogoAgregarLaboratorio()
        }

        btnSincronizar.setOnClickListener {
            viewModel.sincronizarConNube()
        }
    }

    private fun setupRecyclerView() {
        adapter = LaboratorioAdapter { laboratorio ->
            val intent = Intent(this, EquiposActivity::class.java)
            intent.putExtra("LABORATORIO_ID", laboratorio.id)
            intent.putExtra("LABORATORIO_NOMBRE", laboratorio.nombre)
            intent.putExtra("LABORATORIO_EDIFICIO", laboratorio.edificio)
            startActivity(intent)
        }

        rvLaboratorios.layoutManager = LinearLayoutManager(this)
        rvLaboratorios.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allLaboratorios.observe(this) { laboratorios ->
            adapter.submitList(laboratorios)
        }

        viewModel.sincronizacionEstado.observe(this) { estado ->
            when (estado) {
                is SincronizacionEstado.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    btnSincronizar.isEnabled = false
                }
                is SincronizacionEstado.Success -> {
                    progressBar.visibility = View.GONE
                    btnSincronizar.isEnabled = true
                    Toast.makeText(this, estado.message, Toast.LENGTH_LONG).show()
                }
                is SincronizacionEstado.Error -> {
                    progressBar.visibility = View.GONE
                    btnSincronizar.isEnabled = true
                    Toast.makeText(this, "Error: ${estado.message}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    progressBar.visibility = View.GONE
                    btnSincronizar.isEnabled = true
                }
            }
        }
    }

    private fun mostrarDialogoAgregarLaboratorio() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_laboratorio, null)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombreLaboratorio)
        val etEdificio = dialogView.findViewById<TextInputEditText>(R.id.etEdificio)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val edificio = etEdificio.text.toString().trim()

            if (nombre.isEmpty() || edificio.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val laboratorio = Laboratorio(nombre = nombre, edificio = edificio)
            viewModel.insertLaboratorio(laboratorio)
            dialog.dismiss()
            Toast.makeText(this, "Laboratorio agregado", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}
