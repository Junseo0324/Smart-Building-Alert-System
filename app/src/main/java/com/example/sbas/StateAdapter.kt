package com.example.sbas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbas.databinding.StateItemBinding

class StateAdapter(val itemList: ArrayList<FireItem>):
RecyclerView.Adapter<StateAdapter.StateViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateAdapter.StateViewHolder {
        val binding = StateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StateAdapter.StateViewHolder, position: Int) {
        holder.tv_sensor.text = itemList[position].sensor
        holder.tv_state.text = itemList[position].state
        holder.tv_time.text = itemList[position].time
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class StateViewHolder(val binding: StateItemBinding) : RecyclerView.ViewHolder(binding.root){
        val tv_sensor = binding.tvSensor
        val tv_state = binding.tvState
        val tv_time = binding.tvTime


        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {
        }
    }

    var itemClickListener: OnItemClickListener? = null


}