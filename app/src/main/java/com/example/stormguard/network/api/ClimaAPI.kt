package com.example.stormguard.network.api

import com.example.stormguard.data.ClimaDataRemoto
import com.example.stormguard.data.UbicacionRemota
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaAPI {

    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "7059cc6fcf8e414db50122856241303"
    }

    @GET("search.json")
    suspend fun searchLocation(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String,
    ): Response<List<UbicacionRemota>>

    @GET("forecast.json")
    suspend fun getClimaData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String,
    ): Response<ClimaDataRemoto>

}
