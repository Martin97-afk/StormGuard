package com.example.stormguard.dependecy_injection

import com.example.stormguard.fragmentos.Home.HomeVistaModelo
import com.example.stormguard.fragmentos.ubicacion.UbicacionVistaModelo
import com.example.stormguard.network.repositorio.ClimaDataRepositorio
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vistaModeloModulo = module {
    viewModel { HomeVistaModelo(climaDataRepositorio = get()) }
    viewModel { UbicacionVistaModelo(climaDataRepositorio = get()) }
}