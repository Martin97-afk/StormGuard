package com.example.stormguard.network.repositorio

import android.annotation.SuppressLint
import android.location.Geocoder
import com.example.stormguard.data.ClimaDataRemoto
import com.example.stormguard.data.CurrentLocation
import com.example.stormguard.data.UbicacionRemota
import com.example.stormguard.network.api.ClimaAPI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class ClimaDataRepositorio(private val climaAPI: ClimaAPI) {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        onsuccess: (currentLocation: CurrentLocation) -> Unit,
        onFailure: () -> Unit

    ) {
        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            location ?: onFailure()
            onsuccess(
                CurrentLocation(
                    latitude = location.latitude,
                    longitude = location.longitude

                )
            )
        }.addOnFailureListener {onFailure()}
    }


    @Suppress("DEPRECACIÓN")
    fun updateAddressText(
        currentLocation: CurrentLocation,
        geocoder: Geocoder
    ): CurrentLocation {
        val latitude = currentLocation.latitude ?: return currentLocation
        val longitude = currentLocation.longitude ?: return currentLocation
        return geocoder.getFromLocation(latitude, longitude,1)?.let { addresses ->
            val address = addresses[0]
            val addressText = StringBuilder()
            addressText.append(address.locality).append(", ")
            addressText.append(address.adminArea).append(", ")
            addressText.append(address.countryName)
            currentLocation.copy(
                location = addressText.toString()
            )

        }?: currentLocation
    }

     suspend fun searchLocation(query: String): List<UbicacionRemota>? {
         val response = climaAPI.searchLocation(query = query)
         return if(response.isSuccessful) response.body() else null
     }

    suspend fun getClimaData(latitude: Double, longitude: Double): ClimaDataRemoto? {
        val response = climaAPI.getClimaData(query = "$latitude, $longitude")
        return if (response.isSuccessful) response.body() else null
    }
}