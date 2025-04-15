package com.example.stormguard.data

import com.google.gson.annotations.SerializedName

data class ClimaDataRemoto(
    val current: ClimaActualRemoto,
    val forecast: PronosticoRemoto

)

data class  ClimaActualRemoto(
    @SerializedName("temp_c") val temperature: Float,
    val condition: ClimaCondicionRemoto,
    @SerializedName("wind_kph") val wind: Float,
    val humidity: Int
)

data class PronosticoRemoto(
    @SerializedName("forecastday") val forecastDay: List<PronosticoDiaRemoto>
)

data class PronosticoDiaRemoto(
    val day: DiaRemoto,
    val hour: List<HoraPronosticoRemoto>
)

data class DiaRemoto(
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int
)

data class HoraPronosticoRemoto(
    val time: String,
    @SerializedName("temp_c") val temperature: Float,
    @SerializedName("feelslike_c") val feelsLikeTemperature: Float,
    val condition: ClimaCondicionRemoto
)

data class ClimaCondicionRemoto(
    val icon: String
)
