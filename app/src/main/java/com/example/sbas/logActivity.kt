package com.example.sbas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sbas.databinding.ActivityLogBinding

class logActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logTitle.text = intent.getStringExtra("data") +"센서로그"




    }
}