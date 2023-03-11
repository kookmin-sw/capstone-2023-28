package com.capstone.traffic.ui.home.route

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

        private val lineColor1 : AppCompatImageView = itemView.findViewById(R.id.lineColor1)
        private val lineColor2 : AppCompatImageView = itemView.findViewById(R.id.lineColor2)

        private val fr1 : FrameLayout = itemView.findViewById(R.id.fr1)
        private val fr2 : FrameLayout = itemView.findViewById(R.id.fr2)

        private val startIv1 : AppCompatImageView = itemView.findViewById(R.id.startIV1)
        private val startIv2 : AppCompatImageView = itemView.findViewById(R.id.startIV2)
        private val startIv1r : AppCompatImageView = itemView.findViewById(R.id.startIV1r)
        private val startIv2r : AppCompatImageView = itemView.findViewById(R.id.startIV2r)

        private val centerIv1 : AppCompatImageView = itemView.findViewById(R.id.centerIV1)
        private val centerIv2 : AppCompatImageView = itemView.findViewById(R.id.centerIV2)
        private val centerIv1r : AppCompatImageView = itemView.findViewById(R.id.centerIV1r)
        private val centerIv2r : AppCompatImageView = itemView.findViewById(R.id.centerIV2r)

        private val endIv1 : AppCompatImageView = itemView.findViewById(R.id.endIV1)
        private val endIv2 : AppCompatImageView = itemView.findViewById(R.id.endIV2)
        private val endIv1r : AppCompatImageView = itemView.findViewById(R.id.endIV1r)
        private val endIv2r : AppCompatImageView = itemView.findViewById(R.id.endIV2r)

        private val upIv : AppCompatImageView = itemView.findViewById(R.id.upIV)
        private val downIv : AppCompatImageView = itemView.findViewById(R.id.downIV)


        init {

            val mainColor = getStationColor(line)
            upIv.backgroundTintList = ColorStateList.valueOf(mainColor)
            downIv.backgroundTintList = ColorStateList.valueOf(mainColor)
            lineColor1.backgroundTintList = ColorStateList.valueOf(mainColor)
            lineColor2.backgroundTintList = ColorStateList.valueOf(mainColor)



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

            if(item.eStartSubway) startIv1r.visibility = View.VISIBLE
            if(item.eCenterSubway) {
                centerIv1r.visibility = View.VISIBLE
            }
            if(item.eEndSubway) endIv1r.visibility = View.VISIBLE
            if(item.eRStartSubway) startIv2r.visibility = View.VISIBLE
            if(item.eRCenterSubway) {
                centerIv2r.visibility = View.VISIBLE
            }
            if(item.eREndSubway) endIv2r.visibility = View.VISIBLE


            if(item.startSubway) startIv1.visibility = View.VISIBLE
            if(item.centerSubway) {
                fr1.visibility = View.INVISIBLE
                centerIv1.visibility = View.VISIBLE
            }
            if(item.endSubway) endIv1.visibility = View.VISIBLE
            if(item.rStartSubway) startIv2.visibility = View.VISIBLE
            if(item.rCenterSubway) {
                centerIv2.visibility = View.VISIBLE
                fr2.visibility = View.INVISIBLE
            }
            if(item.rEndSubway) endIv2.visibility = View.VISIBLE
        }
    }
}