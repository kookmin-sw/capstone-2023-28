package com.capstone.traffic.ui.inform.ranking

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.model.network.twitter.Data

class RankingAdapter(private val context: Context) : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    var datas = listOf<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_rank, parent,false)
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
        private val countTv = itemView.findViewById<TextView>(R.id.countTV)
        private val lineTv = itemView.findViewById<AppCompatTextView>(R.id.lineTV)
        private val lineCv = itemView.findViewById<CardView>(R.id.lineCV)
        private val rankTv = itemView.findViewById<TextView>(R.id.rankTV)

        fun bind(item: Data) {
            val mainColor = getStationColor(item.name.first().toString())
            countTv.text = item.count
            lineTv.text = item.name
            lineCv.backgroundTintList = ColorStateList.valueOf(mainColor)
            rankTv.text = item.rank
        }
    }
}