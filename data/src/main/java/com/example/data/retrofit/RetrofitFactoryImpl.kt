package com.example.data.retrofit

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactoryImpl(
    private val okHttpClient: OkHttpClient
) : RetrofitFactory {

    override fun createRetrofit(gson: Gson): Retrofit {

        val okHttpBuilder = okHttpClient.newBuilder()

        val loggingInterceptor = HttpLoggingInterceptor()
         loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpBuilder.addInterceptor(loggingInterceptor)

        okHttpBuilder
            .connectTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("")
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))

        return builder.build()
    }

    companion object {
        private const val TIMEOUT_SECS: Long = 120
    }

}