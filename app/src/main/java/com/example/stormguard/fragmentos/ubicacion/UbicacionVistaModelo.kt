package com.example.stormguard.fragmentos.ubicacion

import android.provider.CallLog.Locations
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stormguard.data.UbicacionRemota
import com.example.stormguard.network.repositorio.ClimaDataRepositorio
import kotlinx.coroutines.launch
import retrofit2.http.Query
import java.lang.NullPointerException

class UbicacionVistaModelo(private val climaDataRepositorio: ClimaDataRepositorio) : ViewModel() {

    private val _searchResult = MutableLiveData<SearchResultDataState>()
    val searchResult: LiveData<SearchResultDataState> get() = _searchResult

    fun searchLocation(query: String) {
        viewModelScope.launch {
            emitSearchResultUiState(isLoading = true)
            val searchResult = climaDataRepositorio.searchLocation(query)
            if(searchResult.isNullOrEmpty()) {
                emitSearchResultUiState(error = "Ubicación no encontrada, por favor inténtalo de nuevo.")
            }else  {
                emitSearchResultUiState(locations = searchResult)
            }
        }
    }


    private fun emitSearchResultUiState(
        isLoading: Boolean = false,
        locations: List<UbicacionRemota>? = null,
        error: String? = null
    ) {
     val searchResultDataState = SearchResultDataState(isLoading, locations, error)
        _searchResult.value = searchResultDataState
    }


    data class SearchResultDataState(
        val isLoading: Boolean,
        val locations: List<UbicacionRemota>?,
        val error: String?
    )

}