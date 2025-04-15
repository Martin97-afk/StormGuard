package com.example.stormguard.fragmentos.Home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.stormguard.data.CurrentClima
import com.example.stormguard.data.CurrentLocation
import com.example.stormguard.data.LiveDataEvent
import com.example.stormguard.data.Pronostico
import com.example.stormguard.network.repositorio.ClimaDataRepositorio
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeVistaModelo(private val climaDataRepositorio: ClimaDataRepositorio) : ViewModel() {

    //region Ubicación actual
    private val _currentLocation = MutableLiveData<LiveDataEvent<CurrentLocationDateState>>()
    val currentLocation: LiveData<LiveDataEvent<CurrentLocationDateState>> get() = _currentLocation

    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        viewModelScope.launch {
            emitCurrentLocationUiState(isLoading = true)
            climaDataRepositorio.getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onsuccess = {currentLocation ->
                    updateAddressText(currentLocation, geocoder)
                },
                onFailure = {
                    emitCurrentLocationUiState(error = "no se puede recuperar la ubicación actual")
                }
            )
        }
    }



    private fun updateAddressText(currentLocation: CurrentLocation, geocoder: Geocoder) {
        viewModelScope.launch {
            kotlin.runCatching {
                climaDataRepositorio.updateAddressText(currentLocation, geocoder)
            }.onSuccess {location ->
                emitCurrentLocationUiState(currentLocation = location)
            }.onFailure {
                emitCurrentLocationUiState(
                    currentLocation = currentLocation.copy(
                        location = "N/A"
                    )
                )
            }

        }

    }



    private fun emitCurrentLocationUiState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        error: String? = null

    ) {
        val currentLocationDateState = CurrentLocationDateState(isLoading, currentLocation, error)
        _currentLocation.value = LiveDataEvent(currentLocationDateState)
    }


    data class CurrentLocationDateState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )
    //endregion

    //region Clima Data

    private val _climaData = MutableLiveData<LiveDataEvent<ClimaDataState>> ()
    val climaData: LiveData<LiveDataEvent<ClimaDataState>> get() = _climaData

    fun getClimaData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            emitClimaDataUiState(isLoading = true)
            climaDataRepositorio.getClimaData(latitude, longitude)?.let { climaData ->
                emitClimaDataUiState(
                    currentClima = CurrentClima(
                        icon = climaData.current.condition.icon,
                        temperature = climaData.current.temperature,
                        wind = climaData.current.wind,
                        humidity = climaData.current.humidity,
                        chanceOfRain = climaData.forecast.forecastDay.first().day.chanceOfRain
                    ),
                    forecast = climaData.forecast.forecastDay.first().hour.map {
                        Pronostico(
                            time = getPronosticoTime(it.time),
                            temperature = it.temperature,
                            feelsLikeTemperature = it.feelsLikeTemperature,
                            icon = it.condition.icon
                        )

                    }
                )
            } ?: emitClimaDataUiState(error = "no se pueden obtener datos meteorológicos")
        }
    }

    private fun emitClimaDataUiState(
        isLoading: Boolean = false,
        currentClima: CurrentClima? = null,
        forecast: List<Pronostico>? = null,
        error: String? = null
    ) {
        val climaDataState = ClimaDataState(isLoading, currentClima, forecast, error)
        _climaData.value = LiveDataEvent(climaDataState)
    }

    data class ClimaDataState(
        val isLoading: Boolean,
        val currentClima: CurrentClima?,
        val forecast: List<Pronostico>?,
        val error: String?

    )

    private fun getPronosticoTime(datetime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = pattern.parse(datetime) ?: return datetime
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }

    //endregion
}