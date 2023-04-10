package com.capstone.traffic.ui.route.route.search.arrivalInform

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.model.network.seoul.arrive.RealtimeArrivalList

class ArrivalInformAdapter(private val context : Context) : RecyclerView.Adapter<ArrivalInformAdapter.ViewHolder>() {

    var datas = listOf<RealtimeArrivalList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_subway_arrival_inform, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    private fun getStationColor(line : String) : Int {
        val color = mapOf("1" to R.color.hs1,
            "2" to R.color.hs2,
            "3" to R.color.hs3,
            "4" to R.color.hs4,
            "5" to R.color.hs5,
            "6" to R.color.hs6,
            "7" to R.color.hs7,
            "8" to R.color.hs8,
            "9" to R.color.hs9)
        return if(color[line] != null) context.resources.getColor(color[line]!!) else context.resources.getColor(R.color.black)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stationCv = itemView.findViewById<CardView>(R.id.station_cv)
        private val stationNm = itemView.findViewById<TextView>(R.id.stationName_tv)
        private val trainNm = itemView.findViewById<TextView>(R.id.trainName_tv)
        private val timeNm = itemView.findViewById<TextView>(R.id.trainTime_tv)
        fun bind(item: RealtimeArrivalList) {
            stationCv.backgroundTintList =  ColorStateList.valueOf(getStationColor(item.subwayId.last().toString()))
            stationNm.text = "${item.subwayId.last()} 호선"
            trainNm.text = item.trainLineNm
            timeNm.text = item.arvlMsg2
        }
    }
}