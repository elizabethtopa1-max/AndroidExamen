package com.example.calculadora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.calculadora.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón para navegar al primer fragmento
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        // Botón para calcular el presupuesto
        binding.btnCalcularPresupuesto.setOnClickListener {
            calcularPresupuesto()
        }
    }

    private fun calcularPresupuesto() {
        // Obtener valores de los campos
        val distanciaStr = binding.etDistancia.text.toString().trim()
        val rendimientoStr = binding.etRendimiento.text.toString().trim()
        val precioStr = binding.etPrecioCombustible.text.toString().trim()

        // Validación 1: Campos vacíos
        var hayError = false

        if (distanciaStr.isEmpty()) {
            binding.etDistancia.error = getString(R.string.error_campo_vacio)
            hayError = true
        }

        if (rendimientoStr.isEmpty()) {
            binding.etRendimiento.error = getString(R.string.error_campo_vacio)
            hayError = true
        }

        if (precioStr.isEmpty()) {
            binding.etPrecioCombustible.error = getString(R.string.error_campo_vacio)
            hayError = true
        }

        if (hayError) return

        // Conversión de String a Double
        val distancia = distanciaStr.toDoubleOrNull() ?: 0.0
        val rendimiento = rendimientoStr.toDoubleOrNull() ?: 0.0
        val precio = precioStr.toDoubleOrNull() ?: 0.0

        // Validación 2: Rendimiento no puede ser 0 (evitar división por cero)
        if (rendimiento == 0.0) {
            binding.etRendimiento.error = getString(R.string.error_rendimiento_cero)
            return
        }

        // Cálculos
        val galonesNecesarios = distancia / rendimiento
        val costoTotal = galonesNecesarios * precio

        // Mostrar resultado formateado a 2 decimales
        val resultado = "Necesitas ${String.format("%.2f", galonesNecesarios)} galones.\nCosto total: $${String.format("%.2f", costoTotal)}"
        binding.tvResultado.text = resultado
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
