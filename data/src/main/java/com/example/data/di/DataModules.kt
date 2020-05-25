package com.example.data.di

import com.example.data.retrofit.RetrofitFactory
import com.example.data.retrofit.RetrofitFactoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module.module
import retrofit2.Retrofit

val dataModule = module(override = true) {
    single<Gson> { GsonBuilder().setLenient().create() }
    single<RetrofitFactory> { RetrofitFactoryImpl(get()) }
    single<Retrofit> { get<RetrofitFactory>().createRetrofit(get()) }
}