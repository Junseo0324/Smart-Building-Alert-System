package com.example.sbas

import android.app.Application
import com.example.sbas.retrofit.FireNetworkService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    companion object {
        private val BASE_URL ="http://localhost:8888"

        var networkService: FireNetworkService
        val gson = GsonBuilder().setLenient().create()
        private val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        init {
            networkService = retrofit.create(FireNetworkService::class.java)
        }
    }
}