package com.example.sbas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sbas.databinding.ActivityMainBinding

class logActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        val binding = ActivityMainBinding.inflate(layoutInflater)


    }
}