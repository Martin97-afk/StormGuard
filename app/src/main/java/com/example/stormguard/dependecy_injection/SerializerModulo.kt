package com.example.stormguard.dependecy_injection

import com.google.gson.Gson
import org.koin.dsl.module

val serializerModulo = module {
    single { Gson() }
}