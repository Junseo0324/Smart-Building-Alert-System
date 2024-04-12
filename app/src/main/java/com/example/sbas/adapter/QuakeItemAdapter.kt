package com.example.sbas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbas.data.QuakeModel
import com.example.sbas.databinding.QuakeItemBinding

class QuakeItemAdapter(private val itemList: ArrayList<QuakeModel>) :
    RecyclerView.Adapter<QuakeItemAdapter.QuakeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuakeViewHolder {
        val binding = QuakeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuakeViewHolder, position: Int) {
        holder.vibrationText.text = itemList[position].vibrationSensor
        holder.timeText.text = itemList[position].sensorTime
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    inner class QuakeViewHolder(private val binding: QuakeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val vibrationText = binding.tvData1
        val timeText = binding.tvTime
    }

}