package com.example.sbas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sbas.databinding.ActivityStateBinding

class StateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = Intent(this,logActivity::class.java)
        super.onCreate(savedInstanceState)

        val binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemList = ArrayList<FireItem>()

        itemList.add(FireItem("불꽃탐지","0","14:30"))
        itemList.add(FireItem("온도","21C","13:30"))
        itemList.add(FireItem("CO","16[ppm]","06:34"))


        val stateAdapter = StateAdapter(itemList)
        stateAdapter.notifyDataSetChanged()

        binding.rv.adapter = stateAdapter
        binding.rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        stateAdapter.itemClickListener = object : StateAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val item:String = itemList[position].sensor
                Log.v("이름",item)
                intent.putExtra("data",item)
                startActivity(intent)
//                val item = itemList[position]
//                Toast.makeText(applicationContext,"${item.sensor} 클릭함",Toast.LENGTH_SHORT).show()

            }
        }



        

    }
}