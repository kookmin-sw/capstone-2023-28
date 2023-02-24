package com.capstone.traffic.ui.home.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

class BookmarkAdapter(private val dataList: MutableMap<String, String>) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val bookmarkTitle: TextView = view.findViewById<TextView>(R.id.bookmarkTitle)
        val bookmarkPreview: TextView = view.findViewById<TextView>(R.id.bookmarkPreview)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_layout_fragment_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = dataList.keys.toList()[position]
        val value = dataList[key]
        holder.bookmarkTitle.text = key
        holder.bookmarkPreview.text = value
    }
}