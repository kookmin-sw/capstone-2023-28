package com.capstone.traffic.ui.home.route

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.ui.home.board.bulletinBoard.ArriveInformActivity
import com.capstone.traffic.ui.home.route.dataClass.NeighborLineData
import com.capstone.traffic.ui.home.route.dataClass.SubwayExpressData

class SubwayExpressAdapter(private val context: Context, private val r : Int, private val line : String) : RecyclerView.Adapter<SubwayExpressAdapter.ViewHolder>() {

    var datas = listOf<SubwayExpressData>()
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
        private val subWayName: TextView = itemView.findViewById(R.id.stationName)
        private val eStartSW : AppCompatImageView = itemView.findViewById(R.id.EstartSubway)
        private val eCenterSW : AppCompatImageView = itemView.findViewById(R.id.EcenterSubway)
        private val eEndSW : AppCompatImageView = itemView.findViewById(R.id.EendSubway)
        private val eRStartSW : AppCompatImageView = itemView.findViewById(R.id.EreverseStartSubway)
        private val eRCenterSW : AppCompatImageView = itemView.findViewById(R.id.EreverseCenterSubway)
        private val eREndSW : AppCompatImageView = itemView.findViewById(R.id.EreverseEndSubway)
        private val startSW : AppCompatImageView = itemView.findViewById(R.id.startSubway)
        private val centerSW : AppCompatImageView = itemView.findViewById(R.id.centerSubway)
        private val endSW : AppCompatImageView = itemView.findViewById(R.id.endSubway)
        private val rStartSW : AppCompatImageView = itemView.findViewById(R.id.reverseStartSubway)
        private val rCenterSW : AppCompatImageView = itemView.findViewById(R.id.reverseCenterSubway)
        private val rEndSW : AppCompatImageView = itemView.findViewById(R.id.reverseEndSubway)

        private val circle1 = itemView.findViewById<AppCompatImageView>(R.id.circle1)
        private val circle2 = itemView.findViewById<AppCompatImageView>(R.id.circle2)
        private val circle3 = itemView.findViewById<AppCompatImageView>(R.id.circle3)
        private val circle4 = itemView.findViewById<AppCompatImageView>(R.id.circle4)
        private val line1 = itemView.findViewById<AppCompatImageView>(R.id.line1)
        private val line2 = itemView.findViewById<AppCompatImageView>(R.id.line2)
        private val line3 = itemView.findViewById<AppCompatImageView>(R.id.line3)
        private val line4 = itemView.findViewById<AppCompatImageView>(R.id.line4)
        private val line5 = itemView.findViewById<AppCompatImageView>(R.id.line5)
        private val line6 = itemView.findViewById<AppCompatImageView>(R.id.line6)
        private val line7 = itemView.findViewById<AppCompatImageView>(R.id.line7)
        private val line8 = itemView.findViewById<AppCompatImageView>(R.id.line8)


        init {

            val mainColor = getStationColor(line)
            circle1.backgroundTintList = ColorStateList.valueOf(mainColor)
            circle2.backgroundTintList = ColorStateList.valueOf(mainColor)
            circle3.backgroundTintList = ColorStateList.valueOf(mainColor)
            circle4.backgroundTintList = ColorStateList.valueOf(mainColor)
            startSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            centerSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            endSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            rStartSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            rCenterSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            rEndSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            eStartSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            eCenterSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            eEndSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            eRStartSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            eRCenterSW.backgroundTintList = ColorStateList.valueOf(mainColor)
            eREndSW.backgroundTintList = ColorStateList.valueOf(mainColor)

            line1.setBackgroundColor(mainColor)
            line2.setBackgroundColor(mainColor)
            line3.setBackgroundColor(mainColor)
            line4.setBackgroundColor(mainColor)
            line5.setBackgroundColor(mainColor)
            line6.setBackgroundColor(mainColor)
            line7.setBackgroundColor(mainColor)
            line8.setBackgroundColor(mainColor)



            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val intent = Intent(context,ArriveInformActivity::class.java)
                    val nlData = NeighborLineData(center = datas[adapterPosition].subwayStation, line = line)
                    if(adapterPosition == 0){
                        nlData.right = datas[adapterPosition + 1].subwayStation
                    }
                    else if(adapterPosition + 1 == datas.size) {
                        nlData.left = datas[adapterPosition - 1].subwayStation
                    }
                    else {
                        nlData.right = datas[adapterPosition + 1].subwayStation
                        nlData.left = datas[adapterPosition - 1].subwayStation
                    }

                    intent.putExtra("neighbor", nlData)
                    context.startActivity(intent)
                }
            }
        }
        fun bind(item: SubwayExpressData) {
            subWayName.text = item.subwayStation
            if(item.eStartSubway) eStartSW.visibility = View.VISIBLE
            if(item.eCenterSubway) eCenterSW.visibility = View.VISIBLE
            if(item.eEndSubway) eEndSW.visibility = View.VISIBLE
            if(item.eRStartSubway) eRStartSW.visibility = View.VISIBLE
            if(item.eRCenterSubway) eRCenterSW.visibility = View.VISIBLE
            if(item.eREndSubway) eREndSW.visibility = View.VISIBLE
            if(item.startSubway) startSW.visibility = View.VISIBLE
            if(item.centerSubway) centerSW.visibility = View.VISIBLE
            if(item.endSubway) endSW.visibility = View.VISIBLE
            if(item.rStartSubway) rStartSW.visibility = View.VISIBLE
            if(item.rCenterSubway) rCenterSW.visibility = View.VISIBLE
            if(item.rEndSubway) rEndSW.visibility = View.VISIBLE
        }
    }
}