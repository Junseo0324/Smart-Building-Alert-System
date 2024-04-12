package com.example.sbas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sbas.data.FireModel
import com.example.sbas.data.GasModel
import com.example.sbas.data.QuakeModel
import com.example.sbas.databinding.ActivityStateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fireList = ArrayList<FireModel>()
        // 통신에서 itemList = response.body()로 데이터만 받아오고 밖에서 설정하는걸로 해보자.

        val firecall : Call<ArrayList<FireModel>> = MyApplication.networkService.getFireData()

        firecall?.enqueue(object : Callback<ArrayList<FireModel>> {
            override fun onResponse(call: Call<ArrayList<FireModel>>, response: Response<ArrayList<FireModel>>) {
                if(response.isSuccessful){
                    Log.d("fire 성공",response.body().toString());
                    fireList = response.body()!!
                    binding.fireTv.text = fireList[fireList.size - 1].flame
                    binding.temperTv.text = fireList[fireList.size - 1].temperature
                    binding.fireTimeTv.text = fireList[fireList.size - 1].sensorTime
                }
            }

            override fun onFailure(call: Call<ArrayList<FireModel>>, t: Throwable) {
                Log.d("화재 통신", "실패.......")
            }
        })
        var gasList = ArrayList<GasModel>()
        val gascall : Call<ArrayList<GasModel>> = MyApplication.networkService.getGasData()

        gascall?.enqueue(object : Callback<ArrayList<GasModel>>{
            override fun onResponse(call: Call<ArrayList<GasModel>>, response: Response<ArrayList<GasModel>>) {
                if(response.isSuccessful){
                    Log.d("gas 성공", response.body().toString())
                    gasList = response.body()!!
                    binding.coTv.text = gasList[gasList.size - 1].co
                    binding.flamTv.text = gasList[gasList.size - 1].flammable
                    binding.gasTimeTv.text = gasList[gasList.size - 1].sensorTime
                }
            }

            override fun onFailure(p0: Call<ArrayList<GasModel>>, p1: Throwable) {
                Log.d("가스 통신","실패....")
            }
        })


        var quakeList = ArrayList<QuakeModel>()
        val quakecall : Call<ArrayList<QuakeModel>> = MyApplication.networkService.getQuakeData()

        quakecall?.enqueue(object : Callback<ArrayList<QuakeModel>>{
            override fun onResponse(call: Call<ArrayList<QuakeModel>>, response: Response<ArrayList<QuakeModel>>) {
                if(response.isSuccessful){
                    Log.d("quake 성공",response.body().toString())
                    quakeList = response.body()!!
                    binding.vibrationTv.text = quakeList[quakeList.size - 1].vibrationSensor
                    binding.quakeTimeTv.text = quakeList[quakeList.size - 1].sensorTime
                }
            }

            override fun onFailure(p0: Call<ArrayList<QuakeModel>>, p1: Throwable) {
                Log.d("지진 통신", " 실패.....")
            }
        })


        binding.fireTitle.setOnClickListener {
            getLog(binding.fireTitle.text)
        }
        binding.gasTitle.setOnClickListener {
            getLog(binding.gasTitle.text)
        }
        binding.quakeTitle.setOnClickListener {
            getLog(binding.quakeTitle.text)
        }

    }

    private fun getLog(msg: CharSequence) {
        val intent = Intent(this, LogActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }
}