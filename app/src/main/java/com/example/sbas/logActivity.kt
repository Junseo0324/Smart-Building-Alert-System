package com.example.sbas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sbas.databinding.ActivityLogBinding

class logActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logTitle.text = intent.getStringExtra("data") +" 센서로그"

        val itemList = ArrayList<LogItem>()

        itemList.add(LogItem("0","14:32"))
        itemList.add(LogItem("1","21:49"))
        itemList.add(LogItem("2","06:32"))




        val logAdapter = LogAdapter(itemList)
        logAdapter.notifyDataSetChanged()

        binding.logRv.adapter = logAdapter
        binding.logRv.layoutManager = LinearLayoutManager(this)


    }
}