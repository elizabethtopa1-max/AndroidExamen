package com.example.calculadora.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadora.R
import com.example.calculadora.data.entities.Equipo
import com.example.calculadora.data.entities.EstadoEquipo

class EquipoAdapter(
    private val onEquipoEdit: (Equipo) -> Unit,
    private val onEquipoDelete: (Equipo) -> Unit
) : ListAdapter<Equipo, EquipoAdapter.EquipoViewHolder>(EquipoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = getItem(position)
        holder.bind(equipo)
    }

    inner class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEquipo)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstadoEquipo)
        private val ivEdit: ImageView = itemView.findViewById(R.id.ivEditEquipo)
        private val ivDelete: ImageView = itemView.findViewById(R.id.ivDeleteEquipo)

        fun bind(equipo: Equipo) {
            tvNombre.text = equipo.nombre

            val estadoTexto = when (equipo.estado) {
                EstadoEquipo.OPERATIVO -> "Estado: Operativo"
                EstadoEquipo.DANADO -> "Estado: Dañado"
                EstadoEquipo.PENDIENTE -> "Estado: Pendiente"
            }
            tvEstado.text = estadoTexto

            ivEdit.setOnClickListener {
                onEquipoEdit(equipo)
            }

            ivDelete.setOnClickListener {
                onEquipoDelete(equipo)
            }
        }
    }

    private class EquipoDiffCallback : DiffUtil.ItemCallback<Equipo>() {
        override fun areItemsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem == newItem
        }
    }
}
