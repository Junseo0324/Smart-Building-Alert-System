package com.example.sbas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sbas.data.FireModel
import com.example.sbas.databinding.StateItemBinding

class FireItemAdapter(val itemList: ArrayList<FireModel>):
RecyclerView.Adapter<FireItemAdapter.FireItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireItemViewHolder {
        val binding = StateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FireItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FireItemViewHolder, position: Int) {
        holder.tv_flame.text = itemList[position].flame
        holder.tv_temperature.text = itemList[position].temperature
        holder.tv_time.text = itemList[position].sensorTime
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class FireItemViewHolder(val binding: StateItemBinding) : RecyclerView.ViewHolder(binding.root){
        val tv_flame = binding.tvData1
        val tv_temperature = binding.tvData2
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