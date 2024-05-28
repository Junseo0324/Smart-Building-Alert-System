package com.example.sbas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbas.data.FireModel
import com.example.sbas.databinding.StateItemBinding

class FireItemAdapter(private val itemList: ArrayList<FireModel>):
RecyclerView.Adapter<FireItemAdapter.FireItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireItemViewHolder {
        val binding = StateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FireItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FireItemViewHolder, position: Int) {
        holder.flameText.text = itemList[position].flame
        holder.temperatureText.text = itemList[position].temperature
        holder.timeText.text = itemList[position].sensorTime
        holder.flameIdText.text = "불꽃 상태"
        holder.temperIdText.text = "온도"
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class FireItemViewHolder(private val binding: StateItemBinding) : RecyclerView.ViewHolder(binding.root){
        val flameText = binding.tvData1
        val temperatureText = binding.tvData2
        val timeText = binding.tvTime
        val flameIdText = binding.dataOneText //불꽃 상태
        val temperIdText = binding.dataTwoText //온도
    }


}