package com.capstone.traffic.ui.inform.demon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

class DemonAdapter(private val context: Context) : RecyclerView.Adapter<DemonAdapter.ViewHolder>(){
    var datas = listOf<Demon>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemonAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_rank, parent,false)
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
        fun bind(item: Demon) {
        }
    }
}