package com.capstone.traffic.ui.inform.demon

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.model.network.demon.Data

class DemonAdapter(private val context: Context) : RecyclerView.Adapter<DemonAdapter.ViewHolder>(){
    var datas = listOf<Data>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemonAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.demon_recyclerview, parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size
    override fun onBindViewHolder(holder: DemonAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeTv : TextView = itemView.findViewById(R.id.emo1_tv)
        val place2Tv : TextView = itemView.findViewById(R.id.emo1_left_tv)
        val timeTv : TextView = itemView.findViewById(R.id.emo2_tv)
        val peopleTv : TextView = itemView.findViewById(R.id.emo3_tv)
        @SuppressLint("SetTextI18n")
        fun bind(item: Data) {
            peopleTv.text = "${item.protest.people!!.split(".")[0]}명"
            timeTv.text = "[${item.date}] ${item.protest.time}"
            placeTv.text = item.protest.place
            place2Tv.text = item.protest.region
        }
    }
}