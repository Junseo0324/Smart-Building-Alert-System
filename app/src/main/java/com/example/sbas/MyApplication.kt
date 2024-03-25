package com.example.sbas

import android.app.Application
import com.example.sbas.retrofit.FireNetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    companion object {
        val BASE_URL = "http://10.0.2.2:8080"


        var networkService: FireNetworkService
        val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init {
            networkService = retrofit.create(FireNetworkService::class.java)
        }
    }
}