package com.example.stormguard.fragmentos.ubicacion

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stormguard.data.UbicacionRemota
import com.example.stormguard.databinding.FragmentoUbicacionBinding
import com.example.stormguard.databinding.ItemContainerUbicacionBinding

class AdaptadorUbicaciones(
    private val onLocationClicked: (UbicacionRemota) -> Unit
) : RecyclerView.Adapter<AdaptadorUbicaciones.LocationViewHolder> (){

    private val locations = mutableListOf<UbicacionRemota>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<UbicacionRemota>) {
        locations.clear()
        locations.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            ItemContainerUbicacionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false

            )
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(ubicacionRemota = locations[position])
    }

    override fun getItemCount(): Int {
        return  locations.size
    }

    inner class LocationViewHolder(
        private val binding: ItemContainerUbicacionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ubicacionRemota: UbicacionRemota) {
            with(ubicacionRemota) {
                val location = "$name, $region, $country"
                binding.texRemoteLocation.text= location
                binding.root.setOnClickListener { onLocationClicked(ubicacionRemota) }
            }
            }
        }
    }
