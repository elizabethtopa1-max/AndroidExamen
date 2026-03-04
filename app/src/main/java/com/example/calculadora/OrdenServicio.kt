package com.example.calculadora

/**
 * Clase de datos que representa una orden de servicio técnico.
 * Maneja los datos del cliente y el servicio seleccionado.
 */
data class OrdenServicio(
    var nombreCliente: String = "",
    var telefono: String = "",
    var correo: String = "",
    var servicioSeleccionado: String = ""
) {
    /**
     * Verifica si todos los campos obligatorios están completos.
     */
    fun isComplete(): Boolean {
        return nombreCliente.isNotBlank() &&
                telefono.isNotBlank() &&
                correo.isNotBlank() &&
                servicioSeleccionado.isNotBlank()
    }

    /**
     * Genera el cuerpo del mensaje para el correo de confirmación.
     */
    fun generarMensajeConfirmacion(): String {
        return """
            Estimado/a $nombreCliente,

            Su orden de servicio ha sido registrada exitosamente.

            Detalles del servicio:
            - Servicio: $servicioSeleccionado
            - Teléfono de contacto: $telefono

            Nos pondremos en contacto con usted a la brevedad.

            Saludos cordiales,
            Equipo de Servicios Técnicos
        """.trimIndent()
    }
}
