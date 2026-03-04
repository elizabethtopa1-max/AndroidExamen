package com.example.calculadora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.calculadora.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Botón para calcular el salario
        binding.btnCalcularSalario.setOnClickListener {
            calcularSalario()
        }
    }

    private fun calcularSalario() {
        // Obtener valores de los campos
        val nombre = binding.etNombreEmpleado.text.toString().trim()
        val horasStr = binding.etHorasTrabajadas.text.toString().trim()
        val valorHoraStr = binding.etValorHora.text.toString().trim()

        // Validación de campos vacíos
        var hayError = false

        if (nombre.isEmpty()) {
            binding.etNombreEmpleado.error = getString(R.string.error_campo_vacio)
            hayError = true
        }

        if (horasStr.isEmpty()) {
            binding.etHorasTrabajadas.error = getString(R.string.error_campo_vacio)
            hayError = true
        }

        if (valorHoraStr.isEmpty()) {
            binding.etValorHora.error = getString(R.string.error_campo_vacio)
            hayError = true
        }

        if (hayError) return

        // Conversión de String a Double
        val horas = horasStr.toDoubleOrNull() ?: 0.0
        val valorHora = valorHoraStr.toDoubleOrNull() ?: 0.0

        // Cálculos
        val subtotal = horas * valorHora
        val descuentoIESS = subtotal * 0.0945  // 9.45% de descuento IESS
        val totalRecibir = subtotal - descuentoIESS

        // Mostrar resultado formateado a 2 decimales
        val resultado = "El pago para $nombre es de $${String.format("%.2f", totalRecibir)}"
        binding.tvResultado.text = resultado
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
