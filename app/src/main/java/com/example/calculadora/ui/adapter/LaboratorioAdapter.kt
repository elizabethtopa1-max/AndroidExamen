package com.example.calculadora.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadora.R
import com.example.calculadora.data.entities.Laboratorio

class LaboratorioAdapter(
    private val onLaboratorioClick: (Laboratorio) -> Unit
) : ListAdapter<Laboratorio, LaboratorioAdapter.LaboratorioViewHolder>(LaboratorioDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaboratorioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laboratorio, parent, false)
        return LaboratorioViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaboratorioViewHolder, position: Int) {
        val laboratorio = getItem(position)
        holder.bind(laboratorio)
    }

    inner class LaboratorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreLaboratorio)
        private val tvEdificio: TextView = itemView.findViewById(R.id.tvEdificioLaboratorio)

        fun bind(laboratorio: Laboratorio) {
            tvNombre.text = laboratorio.nombre
            tvEdificio.text = "Edificio: ${laboratorio.edificio}"

            itemView.setOnClickListener {
                onLaboratorioClick(laboratorio)
            }
        }
    }

    private class LaboratorioDiffCallback : DiffUtil.ItemCallback<Laboratorio>() {
        override fun areItemsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Laboratorio, newItem: Laboratorio): Boolean {
            return oldItem == newItem
        }
    }
}
