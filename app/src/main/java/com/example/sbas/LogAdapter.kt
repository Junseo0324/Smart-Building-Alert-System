package com.example.sbas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbas.databinding.LogItemBinding

class LogAdapter(val itemList: ArrayList<LogItem>) :
    RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogAdapter.LogViewHolder {
        val binding = LogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogAdapter.LogViewHolder, position: Int) {
        holder.tv_state.text = itemList[position].state
        holder.tv_time.text = itemList[position].time
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    inner class LogViewHolder(private val binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_state = binding.stateTv
        val tv_time = binding.timeTv
    }

}