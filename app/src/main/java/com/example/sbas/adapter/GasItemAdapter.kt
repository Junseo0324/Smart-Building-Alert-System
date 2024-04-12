package com.example.sbas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbas.data.GasModel
import com.example.sbas.databinding.StateItemBinding

class GasItemAdapter(private val itemList: ArrayList<GasModel>):
    RecyclerView.Adapter<GasItemAdapter.GasItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasItemViewHolder {
        val binding = StateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GasItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GasItemViewHolder, position: Int) {
        holder.dataOneText.text = itemList[position].flammable
        holder.dataTwoText.text = itemList[position].co
        holder.timeText.text = itemList[position].sensorTime
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class GasItemViewHolder(private val binding: StateItemBinding) : RecyclerView.ViewHolder(binding.root){
        val dataOneText = binding.tvData1
        val dataTwoText = binding.tvData2
        val timeText = binding.tvTime

    }



}