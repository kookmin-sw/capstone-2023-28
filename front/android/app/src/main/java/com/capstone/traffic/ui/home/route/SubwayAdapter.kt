package com.capstone.traffic.ui.home.route

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

class SubwayAdapter(private val context: Context) : RecyclerView.Adapter<SubwayAdapter.ViewHolder>() {

    var datas = mutableListOf<SubwayData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subway_line_two_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val subWayName: TextView = itemView.findViewById(R.id.stationName)
        private val startSW : ImageView = itemView.findViewById(R.id.startSubway)
        private val centerSW : ImageView = itemView.findViewById(R.id.centerSubway)
        private val endSW : ImageView = itemView.findViewById(R.id.endSubway)
        private val rStartSW : ImageView = itemView.findViewById(R.id.reverseStartSubway)
        private val rCenterSW : ImageView = itemView.findViewById(R.id.reverseCenterSubway)
        private val rEndSW : ImageView = itemView.findViewById(R.id.reverseEndSubway)

        fun bind(item: SubwayData) {
            subWayName.text = item.subwayStation
            if(item.startSubway) startSW.visibility = View.VISIBLE
            if(item.centerSubway) centerSW.visibility = View.VISIBLE
            if(item.endSubway) endSW.visibility = View.VISIBLE
            if(item.rStartSubway) rStartSW.visibility = View.VISIBLE
            if(item.rCenterSubway) rCenterSW.visibility = View.VISIBLE
            if(item.rEndSubway) rEndSW.visibility = View.VISIBLE
        }
    }
}