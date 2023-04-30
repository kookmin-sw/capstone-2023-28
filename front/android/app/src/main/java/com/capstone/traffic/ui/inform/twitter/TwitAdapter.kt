package com.capstone.traffic.ui.inform.twitter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.capstone.traffic.R
import com.capstone.traffic.model.network.seoulMetro.Data

class TwitAdapter(private val context : Context) : RecyclerView.Adapter<TwitAdapter.ViewHolder>() {
    var datas = listOf<Data>()
    override fun getItemCount(): Int = datas.size
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwitAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_twitter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TwitAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val timeTv = itemView.findViewById<TextView>(R.id.writeTime)
        val contentTv = itemView.findViewById<TextView>(R.id.contents)

        fun bind(item : Data)
        {
            val time = parseTime(item.info.createdAt)
            timeTv.text = time
            contentTv.text = item.tweet
        }
    }

    private fun parseTime(time : String) : String
    {
        val splitTime = time.split("T")
        if(splitTime.size == 2)
        {
            val leftTime = splitTime[0]
            var rightTime = splitTime[1]
            rightTime = rightTime.split(".")[0] ?: ""

            return "$leftTime $rightTime"
        }
        return ""
    }
}