package com.example.stormguard.model

class Entidades {
    data class Ubicacion(
        val idUbicacion: Long,
        val nombreCiudad: String,
        val latitud: Double,
        val longitud: Double,
        val codigoPostal: String
    )

    data class CondicionesActuales(
        val id: Long,
        val temperatura: Float,
        val humedad: Int,
        val velocidadViento: Float,
        val direccionViento: Float,
        val presionAtmosferica: Float,
        val visibilidad: Float,
        val indiceUV: Float
    )

    data class DatosHistoricos(
        val id: Long,
        val fecha: String,
        val temperatura: Float,
        val humedad: Int,
        val velocidadViento: Float,
        val direccionViento: Float,
        val presionAtmosferica: Float,
        val visibilidad: Float,
        val indiceUV: Float
    )

    data class RegistroAcceso(
        val id: Long,
        val usuario: String,
        val fechaHora: String,
        val accion: String
    )

}