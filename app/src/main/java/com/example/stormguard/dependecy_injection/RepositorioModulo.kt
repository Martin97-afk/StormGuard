package com.example.stormguard.dependecy_injection

import com.example.stormguard.network.api.ClimaAPI
import com.example.stormguard.network.repositorio.ClimaDataRepositorio
import org.koin.dsl.module

val repositorioModulo = module {
    single { ClimaDataRepositorio(climaAPI = get()) }
}