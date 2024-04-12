package com.example.sbas.retrofit

import com.example.sbas.data.FireModel
import com.example.sbas.data.GasModel
import com.example.sbas.data.QuakeModel
import com.example.sbas.data.WarningModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface FireNetworkService {
    @Headers("accept: application/json",
        "content-type: application/json")
    @GET("/fire/list")
    fun getFireData():Call<ArrayList<FireModel>>

    @GET("/gas/list")
    fun getGasData():Call<ArrayList<GasModel>>

    @GET("/quake/list")
    fun getQuakeData():Call<ArrayList<QuakeModel>>

    @POST("/token/save")
    fun sendToken(@Body token: WarningModel):Call<WarningModel>
}