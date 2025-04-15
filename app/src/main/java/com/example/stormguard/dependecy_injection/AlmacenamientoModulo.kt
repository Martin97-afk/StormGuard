package com.example.stormguard.dependecy_injection

import android.content.SharedPreferences
import com.example.stormguard.almacenamiento.PreferenciasCompartidasManager
import org.koin.dsl.module

val almacenamientoModulo = module {
    single { PreferenciasCompartidasManager(context = get(), gson = get()) }
}