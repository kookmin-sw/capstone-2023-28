package com.capstone.traffic.ui.home.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent
import org.w3c.dom.Text

class FeedAdapter(private val dataList: List<Feed>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val feedContent: TextView = view.findViewById<TextView>(R.id.feed_content)
        val feedProfileImage: CardView = view.findViewById<CardView>(R.id.feed_profile_image)
        val feedNickname: TextView = view.findViewById<TextView>(R.id.feed_nickname)
        val feedHashTag: TextView = view.findViewById<TextView>(R.id.feed_hash_tag)
        val feedTime: TextView = view.findViewById<TextView>(R.id.feed_time)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.feedContent.text = data.feedContent
        holder.feedNickname.text = data.feedNickname
        holder.feedHashTag.text = data.feedHashTag.toString()
        holder.feedTime.text = data.feedTime
    }
}