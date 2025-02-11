package com.example.sbas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sbas.adapter.FireItemAdapter
import com.example.sbas.adapter.GasItemAdapter
import com.example.sbas.adapter.QuakeItemAdapter
import com.example.sbas.data.FireModel
import com.example.sbas.data.GasModel
import com.example.sbas.data.QuakeModel
import com.example.sbas.databinding.ActivityLogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var msg: String? = intent.getStringExtra("msg")
        binding.logToolbar.title = "$msg 센서 로그"
        var fireItemList = ArrayList<FireModel>()
        var gasItemList = ArrayList<GasModel>()
        var quakeItemList = ArrayList<QuakeModel>()

        val fireCall: Call<ArrayList<FireModel>> = MyApplication.networkService.getFireData()
        val gasCall: Call<ArrayList<GasModel>> = MyApplication.networkService.getGasData()
        val quakeCall: Call<ArrayList<QuakeModel>> = MyApplication.networkService.getQuakeData()

        when(msg){
            "화재 센서" -> {
                binding.logRvTwo.visibility = View.INVISIBLE
                fireCall?.enqueue(object :Callback<ArrayList<FireModel>>{
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
            "가스 누출 센서"->{
                binding.logRvTwo.visibility = View.INVISIBLE
                gasCall?.enqueue(object : Callback<ArrayList<GasModel>>{
                    override fun onResponse(call: Call<ArrayList<GasModel>>, response: Response<ArrayList<GasModel>>) {
                        if(response.isSuccessful){
                            gasItemList = response.body()!!
                            val gasAdapter = GasItemAdapter(gasItemList)
                            gasAdapter.notifyDataSetChanged()

                            binding.logRv.adapter = gasAdapter
                            binding.logRv.layoutManager = LinearLayoutManager(applicationContext)
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<GasModel>>, response: Throwable) {
                        Log.d("통신","실패..")
                    }
                })
            }
            "지진 감지 센서"->{
                binding.logRv.visibility = View.GONE
                binding.logRvTwo.visibility = View.VISIBLE
                quakeCall?.enqueue(object : Callback<ArrayList<QuakeModel>>{
                    override fun onResponse(call: Call<ArrayList<QuakeModel>>, response: Response<ArrayList<QuakeModel>>) {
                        if(response.isSuccessful){
                            quakeItemList = response.body()!!
                            val quakeAdapter = QuakeItemAdapter(quakeItemList)
                            quakeAdapter.notifyDataSetChanged()
                            binding.logRvTwo.adapter = quakeAdapter
                            binding.logRvTwo.layoutManager = LinearLayoutManager(applicationContext)
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<QuakeModel>>, response: Throwable) {
                        Log.d("통신","실패..")
                    }
                })
            }
            else -> {
                startActivity(Intent(this,MainActivity::class.java))
            }

        }

    }
}