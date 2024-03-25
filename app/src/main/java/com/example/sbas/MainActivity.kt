package com.example.sbas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sbas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var temp : Int = 0      //temp 값 : 0 -> 평소  1 -> 주의 경보 2 -> 위험 경보
        var backgroundState : Int = 0  // backgroundState 값 W : 기본 Y : 주의 R : 화재 G : 지진 B : 가스


        //temp 값에 따라 달라지는 텍스트와 배경색
        if(temp == 0){
            binding.alertText.text = "감지된 위협이 없습니다."
            binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFFFFF"))//White
        }else if(temp == 1){
            binding.alertText.text = "주의 ~~를 확인하십시오."
            binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFEB3B"))// Yellow

        }
        else {
            binding.alertText.text = " 위험 ~~가 동작중입니다."
            //binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFFFFF"))//흰색
        }


        binding.mainSc.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.fireToggle.isChecked = true
                binding.gasToggle.isChecked = true
                binding.eqToggle.isChecked = true
            }else {
                binding.fireToggle.isChecked = false
                binding.gasToggle.isChecked = false
                binding.eqToggle.isChecked = false
            }
        }

        binding.fireToggle.setOnCheckedChangeListener { buttonView, isChecked ->
            //fireToggle이 켜지면 어떤 행위를 할건지에 대한 리스너
            if(isChecked){
                Toast.makeText(applicationContext,"fireToggle on",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"fireToggle off",Toast.LENGTH_SHORT).show()
            }
        }


        //Intent//
        binding.fireText.setOnClickListener{
            startState(binding.fireText.text)
        }
        binding.gasText.setOnClickListener {
            startState(binding.gasText.text)
        }
        binding.eqText.setOnClickListener{
            startState(binding.eqText.text)
        }


    }

    private fun startState(msg: CharSequence) {
        val intent = Intent(this, StateActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }
}