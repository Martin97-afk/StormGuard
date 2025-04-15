package com.example.stormguard.dependecy_injection

import com.example.stormguard.network.api.ClimaAPI
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { okHttpClient() }
    single { retrofit(okHttpClient = get()) }
    factory { climaAPI(retrofit = get()) }
}



private fun okHttpClient() = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30,TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .retryOnConnectionFailure(false)
    .build()

private fun retrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(ClimaAPI.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun climaAPI(retrofit: Retrofit) = retrofit.create(ClimaAPI:: class.java)