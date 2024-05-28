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
        //activity_state.xml의 대한 viewBinding 처리
        val binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fireList = ArrayList<FireModel>()
        // 통신에서 itemList = response.body()로 데이터만 받아오고 밖에서 설정하는걸로 해보자.

        val fireCall : Call<ArrayList<FireModel>> = MyApplication.networkService.getFireData()

        //웹 서버에서 값을 가져오는 메소드 fire,gas,earthquake//
        fireCall?.enqueue(object : Callback<ArrayList<FireModel>> {
            override fun onResponse(call: Call<ArrayList<FireModel>>, response: Response<ArrayList<FireModel>>) {
                if(response.isSuccessful){
                    Log.d("fire Success",response.body().toString());
                    fireList = response.body()!!

                    //text에 가져온 아이템 리스트들 중 가장 마지막 아이템들 적용 -> 최근의 데이터를 보여준다.
                    binding.fireTv.text = fireList[fireList.size - 1].flame
                    binding.temperTv.text = fireList[fireList.size - 1].temperature
                    binding.fireTimeTv.text = fireList[fireList.size - 1].sensorTime

                    //flame 값이 0 or 1로 들어오기 때문에 T F 로 변환
                    if (fireList[fireList.size-1].flame == "0"){
                        binding.fireTv.text = "탐지 X"
                    }
                    else binding.fireTv.text = "불꽃 발생중"
                }
            }
            override fun onFailure(call: Call<ArrayList<FireModel>>, t: Throwable) {
                Log.d("화재 통신", "실패.......")
            }
        })
        var gasList = ArrayList<GasModel>()
        val gasCall : Call<ArrayList<GasModel>> = MyApplication.networkService.getGasData()

        gasCall?.enqueue(object : Callback<ArrayList<GasModel>>{
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
        val quakeCall : Call<ArrayList<QuakeModel>> = MyApplication.networkService.getQuakeData()

        quakeCall?.enqueue(object : Callback<ArrayList<QuakeModel>>{
            override fun onResponse(call: Call<ArrayList<QuakeModel>>, response: Response<ArrayList<QuakeModel>>) {
                if(response.isSuccessful){
                    Log.d("quake 성공",response.body().toString())
                    quakeList = response.body()!!
                    binding.vibrationTv.text = quakeList[quakeList.size - 1].vibrationSensor
                    binding.quakeTimeTv.text = quakeList[quakeList.size - 1].sensorTime
                    if (quakeList[quakeList.size-1].vibrationSensor == "0"){
                        binding.vibrationTv.text = "지진 발생 X"
                    }
                    else binding.vibrationTv.text = "지진 발생 중"
                }
            }

            override fun onFailure(p0: Call<ArrayList<QuakeModel>>, p1: Throwable) {
                Log.d("지진 통신", " 실패.....")
            }
        })

        binding.stateFireLinear.setOnClickListener{
            startLog(binding.fireTitle.text)
        }
        binding.stateGasLinear.setOnClickListener {
            startLog(binding.gasTitle.text)
        }
        binding.stateQuakeLinear.setOnClickListener {
            startLog(binding.quakeTitle.text)
        }
    }

    // msg 에 넣은 값으로 Log의 이름을 구별
    private fun startLog(msg: CharSequence) {
        val intent = Intent(this, LogActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }
}