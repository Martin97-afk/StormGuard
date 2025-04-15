package com.example.stormguard.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class ClimaData

data class CurrentLocation(
    val date: String = getCurrentDate(),
    val location: String = "Elige tu ubicación",
    val latitude: Double? = null,
    val longitude: Double? = null
) : ClimaData()

data class CurrentClima(
    val icon: String,
    val temperature: Float,
    val wind: Float,
    val humidity: Int,
    val chanceOfRain: Int

): ClimaData()

data class Pronostico(
    val time: String,
    val temperature: Float,
    val feelsLikeTemperature: Float,
    val icon: String
) : ClimaData()

private fun getCurrentDate(): String{
    val currentDate = Date()
    val formatter = SimpleDateFormat(" d MMMM yyyy", Locale.getDefault())
    return "Hoy, ${formatter.format(currentDate)}"
}