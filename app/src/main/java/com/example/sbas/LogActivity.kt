package com.example.sbas

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sbas.adapter.FireItemAdapter
import com.example.sbas.data.FireModel
import com.example.sbas.data.GasModel
import com.example.sbas.databinding.ActivityLogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logTitle.text = intent.getStringExtra("msg") +" 센서 로그"

        var fireItemList = ArrayList<FireModel>()
        var gasItemList = ArrayList<GasModel>()

        val call: Call<ArrayList<FireModel>> = MyApplication.networkService.getFireData()

        call?.enqueue(object :Callback<ArrayList<FireModel>>{
            override fun onResponse(call: Call<ArrayList<FireModel>>, response: Response<ArrayList<FireModel>>) {
                if(response.isSuccessful){
                    fireItemList = response.body()!!
                    val fireAdapter = FireItemAdapter(fireItemList)
                    fireAdapter.notifyDataSetChanged()

                    binding.logRv.adapter = fireAdapter
                    binding.logRv.layoutManager = LinearLayoutManager(applicationContext)

                }
            }

            override fun onFailure(call: Call<ArrayList<FireModel>>, response: Throwable) {
                Log.d("통신","실패..")
            }
        })

    }
}