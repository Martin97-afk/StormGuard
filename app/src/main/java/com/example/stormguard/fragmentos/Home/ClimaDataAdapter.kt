package com.example.stormguard.fragmentos.Home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.stormguard.data.ClimaData
import com.example.stormguard.data.CurrentClima
import com.example.stormguard.data.CurrentLocation
import com.example.stormguard.data.Pronostico
import com.example.stormguard.databinding.ItemContainerCurrentClimaBinding
import com.example.stormguard.databinding.ItemContainerCurrrentUbicacionBinding
import com.example.stormguard.databinding.ItemContainerPronosticoBinding
import com.example.stormguard.databinding.ItemContainerUbicacionBinding
import kotlin.coroutines.coroutineContext

class ClimaDataAdapter (
    private val onLocationClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder> () {

    private companion object {
        const val INDEX_CURRENT_LOCATION = 0
        const val INDEX_CURRENT_WEATHER = 1
        const val INDEX_FORECAST = 2
    }

    private val ClimaData = mutableListOf<ClimaData>()


    fun setCurrentLocation(currentLocation: CurrentLocation) {
        if (ClimaData.isEmpty()) {
            ClimaData.add(INDEX_CURRENT_LOCATION, currentLocation)
            notifyItemInserted(INDEX_CURRENT_LOCATION)
        }else {
            ClimaData[INDEX_CURRENT_LOCATION] = currentLocation
            notifyItemChanged(INDEX_CURRENT_LOCATION)
        }
    }

    fun setCurrentClima(currentClima: CurrentClima) {
        if(ClimaData.getOrNull(INDEX_CURRENT_WEATHER )!=null) {
            ClimaData[INDEX_CURRENT_WEATHER] = currentClima
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        }else {
            ClimaData.add(INDEX_CURRENT_WEATHER, currentClima)
            notifyItemInserted(INDEX_CURRENT_WEATHER)
        }
    }

    fun setPronosticoData(pronostico: List<Pronostico>) {
        ClimaData.removeAll { it is Pronostico }
        notifyItemRangeRemoved(INDEX_FORECAST, ClimaData.size)
        ClimaData.addAll(INDEX_FORECAST,pronostico)
        notifyItemRangeChanged(INDEX_FORECAST, ClimaData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemContainerCurrrentUbicacionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            INDEX_FORECAST -> PronosticoViewHolder(
                ItemContainerPronosticoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> CurrentClimaViewHolder(
                ItemContainerCurrentClimaBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CurrentLocationViewHolder ->  holder.bind(ClimaData[position] as CurrentLocation)
            is CurrentClimaViewHolder -> holder.bind(ClimaData[position] as CurrentClima)
            is PronosticoViewHolder -> holder.bind(ClimaData[position] as Pronostico)
        }
    }

    override fun getItemCount(): Int {
        return ClimaData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(ClimaData[position]) {
            is CurrentLocation -> INDEX_CURRENT_LOCATION
            is CurrentClima -> INDEX_CURRENT_WEATHER
            is Pronostico -> INDEX_FORECAST
        }
    }

    inner class CurrentLocationViewHolder(
        private val binding: ItemContainerCurrrentUbicacionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentLocation: CurrentLocation) {
            with(binding) {
                textCurrentDate.text = currentLocation.date
                textCurrentLocation.text= currentLocation.location
                imageCurrentLocation.setOnClickListener { onLocationClicked() }
                textCurrentLocation.setOnClickListener { onLocationClicked() }
            }
        }
    }

    inner class CurrentClimaViewHolder(
        private val binding: ItemContainerCurrentClimaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentClima: CurrentClima) {
            with(binding) {
                imageIcon.load("https:${currentClima.icon}") { crossfade(true) }
                textTemperature.text = String.format("%s\u00B0C", currentClima.temperature)
                textWind.text = String.format("%s km/h", currentClima.wind)
                textHumidity.text = String.format("%s%%", currentClima.humidity)
                textChanceOfRain.text = String.format("%s%%", currentClima.chanceOfRain)
            }
        }
    }

    inner class PronosticoViewHolder(
        private val binding: ItemContainerPronosticoBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(pronostico: Pronostico) {
            with(binding) {
                textTime.text = pronostico.time
                textTemperature.text = String.format("%s\u00B0C", pronostico.temperature)
                textFeelsLikeTemperature.text = String.format("%s\u00B0C", pronostico.feelsLikeTemperature)
                imageIcon.load("https:${pronostico.icon}") {crossfade(true)}
            }
        }
    }
}