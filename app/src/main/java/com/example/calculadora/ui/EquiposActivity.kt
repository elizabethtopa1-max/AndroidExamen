package com.example.calculadora.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadora.R
import com.example.calculadora.data.entities.Equipo
import com.example.calculadora.data.entities.EstadoEquipo
import com.example.calculadora.ui.adapter.EquipoAdapter
import com.example.calculadora.viewmodel.TechAuditViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class EquiposActivity : AppCompatActivity() {

    private lateinit var viewModel: TechAuditViewModel
    private lateinit var adapter: EquipoAdapter
    private lateinit var rvEquipos: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var tvInfoLaboratorio: TextView
    private var laboratorioId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)

        viewModel = ViewModelProvider(this).get(TechAuditViewModel::class.java)

        laboratorioId = intent.getLongExtra("LABORATORIO_ID", 0)
        val laboratorioNombre = intent.getStringExtra("LABORATORIO_NOMBRE") ?: ""
        val laboratorioEdificio = intent.getStringExtra("LABORATORIO_EDIFICIO") ?: ""

        setupViews(laboratorioNombre, laboratorioEdificio)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupViews(nombre: String, edificio: String) {
        tvInfoLaboratorio = findViewById(R.id.tvInfoLaboratorio)
        rvEquipos = findViewById(R.id.rvEquipos)
        fabAgregar = findViewById(R.id.fabAgregarEquipo)

        tvInfoLaboratorio.text = "$nombre - $edificio"

        fabAgregar.setOnClickListener {
            mostrarDialogoAgregarEquipo()
        }
    }

    private fun setupRecyclerView() {
        adapter = EquipoAdapter { equipo ->
            mostrarDialogoEditarEquipo(equipo)
        }

        rvEquipos.layoutManager = LinearLayoutManager(this)
        rvEquipos.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val equipo = adapter.currentList[position]

                AlertDialog.Builder(this@EquiposActivity)
                    .setTitle("Eliminar Equipo")
                    .setMessage("¿Está seguro de eliminar ${equipo.nombre}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        viewModel.deleteEquipo(equipo)
                        Toast.makeText(this@EquiposActivity, "Equipo eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        adapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }
        })

        itemTouchHelper.attachToRecyclerView(rvEquipos)
    }

    private fun observeViewModel() {
        viewModel.getEquiposByLaboratorio(laboratorioId).observe(this) { equipos ->
            adapter.submitList(equipos)
        }
    }

    private fun mostrarDialogoAgregarEquipo() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_equipo, null)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombreEquipo)
        val rgEstado = dialogView.findViewById<RadioGroup>(R.id.rgEstadoEquipo)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val nombre = etNombre.text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el nombre del equipo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val estado = when (rgEstado.checkedRadioButtonId) {
                R.id.rbDanado -> EstadoEquipo.DANADO
                R.id.rbPendiente -> EstadoEquipo.PENDIENTE
                else -> EstadoEquipo.OPERATIVO
            }

            val equipo = Equipo(
                nombre = nombre,
                estado = estado,
                laboratorioId = laboratorioId
            )
            viewModel.insertEquipo(equipo)
            dialog.dismiss()
            Toast.makeText(this, "Equipo agregado", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    private fun mostrarDialogoEditarEquipo(equipo: Equipo) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_equipo, null)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombreEquipo)
        val rgEstado = dialogView.findViewById<RadioGroup>(R.id.rgEstadoEquipo)

        etNombre.setText(equipo.nombre)
        when (equipo.estado) {
            EstadoEquipo.OPERATIVO -> rgEstado.check(R.id.rbOperativo)
            EstadoEquipo.DANADO -> rgEstado.check(R.id.rbDanado)
            EstadoEquipo.PENDIENTE -> rgEstado.check(R.id.rbPendiente)
        }

        dialogView.findViewById<TextView>(R.id.tvTitulo)?.text = "Editar Equipo"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val nombre = etNombre.text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el nombre del equipo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val estado = when (rgEstado.checkedRadioButtonId) {
                R.id.rbDanado -> EstadoEquipo.DANADO
                R.id.rbPendiente -> EstadoEquipo.PENDIENTE
                else -> EstadoEquipo.OPERATIVO
            }

            val equipoActualizado = equipo.copy(nombre = nombre, estado = estado)
            viewModel.updateEquipo(equipoActualizado)
            dialog.dismiss()
            Toast.makeText(this, "Equipo actualizado", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}
