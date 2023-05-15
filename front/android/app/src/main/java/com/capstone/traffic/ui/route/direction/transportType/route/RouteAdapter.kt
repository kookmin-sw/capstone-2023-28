package com.capstone.traffic.ui.route.direction.transportType.route

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

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
        private val tpPlaceTv = itemView.findViewById<TextView>(R.id.transport_place_tv)
        private val tpNameTv = itemView.findViewById<TextView>(R.id.transport_name_tv)
        private val tpIv = itemView.findViewById<AppCompatImageView>(R.id.transport_iv)
        init {

        }
        fun bind(item: Route) {
            if(item.type == "BUS") {
                tpIv.background = ContextCompat.getDrawable(context, R.drawable.icon_bus_default)
            }
            val color = android.graphics.Color.parseColor("#${item.color}")
            tpIv.backgroundTintList = ColorStateList.valueOf(color)
            tpPlaceTv.text = item.station
            tpNameTv.text = item.name
        }
    }
}