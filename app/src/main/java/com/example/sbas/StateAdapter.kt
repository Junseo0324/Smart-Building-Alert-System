package com.example.sbas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StateAdapter(val itemList: ArrayList<FireItem>):
RecyclerView.Adapter<StateAdapter.StateViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateAdapter.StateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.state_item, parent, false)
        return StateViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateAdapter.StateViewHolder, position: Int) {
        holder.tv_sensor.text = itemList[position].sensor
        holder.tv_state.text = itemList[position].state
        holder.tv_time.text = itemList[position].time
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_sensor = itemView.findViewById<TextView>(R.id.tv_sensor)
        val tv_state = itemView.findViewById<TextView>(R.id.tv_state)
        val tv_time = itemView.findViewById<TextView>(R.id.tv_time)
    }


}