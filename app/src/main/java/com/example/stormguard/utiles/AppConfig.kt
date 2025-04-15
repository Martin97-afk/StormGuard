package com.example.stormguard.utiles

import android.app.Application
import com.example.stormguard.dependecy_injection.almacenamientoModulo
import com.example.stormguard.dependecy_injection.networkModule
import com.example.stormguard.dependecy_injection.repositorioModulo
import com.example.stormguard.dependecy_injection.serializerModulo
import com.example.stormguard.dependecy_injection.vistaModeloModulo
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppConfig: Application () {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(
                listOf(
                    repositorioModulo,
                    vistaModeloModulo,
                    serializerModulo,
                    almacenamientoModulo,
                    networkModule

                    )
            )
        }
    }
}