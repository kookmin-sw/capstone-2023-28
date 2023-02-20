package com.capstone.traffic.ui.home.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import org.w3c.dom.Text

class SubwayNowAdapter(private val dataList: List<String>) : RecyclerView.Adapter<SubwayNowAdapter.ViewHolder>() {
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val subwayNowTextView: TextView = view.findViewById<TextView>(R.id.subway_now_textView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subway_now_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.subwayNowTextView.text = data
    }
}