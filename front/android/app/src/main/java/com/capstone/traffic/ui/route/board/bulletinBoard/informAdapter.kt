package com.capstone.traffic.ui.route.board.bulletinBoard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

class informAdapter(private val context: Context, private val r : Int) : RecyclerView.Adapter<informAdapter.ViewHolder>() {

    var datas = listOf<ArrivalInform>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(r, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val arrowTV = itemView.findViewById<TextView>(R.id.arrowTV)
        private val timeTV = itemView.findViewById<TextView>(R.id.timeTV)
        fun bind(item: ArrivalInform) {
            arrowTV.text = item.arrow
            timeTV.text = item.time
        }
    }
}