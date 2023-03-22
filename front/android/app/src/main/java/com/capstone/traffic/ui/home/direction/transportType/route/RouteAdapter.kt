package com.capstone.traffic.ui.home.direction.transportType.route

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.ui.home.direction.transportType.DirectionAdapter
import com.capstone.traffic.ui.home.direction.transportType.DirectionData

class RouteAdapter(private val context: Context) : RecyclerView.Adapter<RouteAdapter.ViewHolder>(){
    var datas = listOf<Route>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.direction_time_recycler, parent,false)
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
        val tpPlaceTv = itemView.findViewById<TextView>(R.id.transport_place_tv)
        val tpNameTv = itemView.findViewById<TextView>(R.id.transport_name_tv)
        init {

        }
        fun bind(item: Route) {
            tpPlaceTv.text = item.station
            tpNameTv.text = item.name
        }
    }
}