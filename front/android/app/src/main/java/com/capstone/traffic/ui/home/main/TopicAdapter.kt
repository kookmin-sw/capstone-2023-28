package com.capstone.traffic.ui.home.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import org.w3c.dom.Text
import kotlin.coroutines.coroutineContext

class TopicAdapter(private val dataList: List<Topic>) : RecyclerView.Adapter<TopicAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topicLine: TextView = view.findViewById<TextView>(R.id.topicLine)
        val topicStation: TextView = view.findViewById<TextView>(R.id.topicStation)
        val topicContent: TextView = view.findViewById<TextView>(R.id.topicContent)
        val topicHeart: TextView = view.findViewById<TextView>(R.id.topicHeart)
        val topicViewNum: TextView = view.findViewById<TextView>(R.id.topicViewNum)
    }
    override fun getItemCount(): Int {
        // 토픽 3개만 보이게
        return 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.topic_layout_fragment_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lineCircleMap = mapOf("1" to R.drawable.shape_circle1, "2" to R.drawable.shape_circle2, "3" to R.drawable.shape_circle3, "4" to R.drawable.shape_circle4, "5" to R.drawable.shape_circle5, "6" to R.drawable.shape_circle6, "7" to R.drawable.shape_circle7, "8" to R.drawable.shape_circle8, "9" to R.drawable.shape_circle9,)
        val data = dataList[position]
        holder.topicLine.background = ContextCompat.getDrawable(holder.itemView.context, lineCircleMap[data.line.toString()]!!)

        holder.topicLine.text = data.line.toString()
        holder.topicStation.text = data.station
        holder.topicContent.text = data.content
        holder.topicHeart.text = data.heart.toString()
        holder.topicViewNum.text = data.view_num.toString()
    }

}