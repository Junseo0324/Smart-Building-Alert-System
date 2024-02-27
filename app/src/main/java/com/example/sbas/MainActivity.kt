package com.example.sbas

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sbas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        var temp : Int = 0      //temp 값 : 0 -> 평소  1 -> 주의 경보 2 -> 위험 경보
        var backgroundState : Int = 0
        // backgorundState 값 //
        // W : 기본
        // Y : 주의
        // R : 화재
        // G : 지진
        // B : 가스


        //temp 값에 따라 달라지는 텍스트와 배경색
        if(temp == 0){
            binding.alertText.setText("감지된 위협이 없습니다.")
            binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFFFFF"))//White
        }else if(temp == 1){
            binding.alertText.setText("주의 ~~를 확인하십시오.")
            binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFEB3B"))// Yellow

        }
        else {
            binding.alertText.setText(" 위험 ~~가 동작중입니다.")
            //binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFFFFF"))//흰색
        }





    }
}