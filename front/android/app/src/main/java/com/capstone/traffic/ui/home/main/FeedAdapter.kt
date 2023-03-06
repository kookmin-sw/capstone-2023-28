package com.capstone.traffic.ui.home.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.traffic.R

class FeedAdapter(private val dataList: List<Feed>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private lateinit var context: Context
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val feedContent: TextView = view.findViewById<TextView>(R.id.feed_content)
        val feedProfileImage: ImageView = view.findViewById<ImageView>(R.id.feed_profile_image)
        val feedNickname: TextView = view.findViewById<TextView>(R.id.feed_nickname)
        val feedHashTag: TextView = view.findViewById<TextView>(R.id.feed_hash_tag)
        val feedTime: TextView = view.findViewById<TextView>(R.id.feed_time)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.feedContent.text = data.feedContent
        holder.feedNickname.text = data.feedNickname
        var hashTagText = ""
        for(tag in data.feedHashTag){
            hashTagText += "#" + tag + " "
        }
        data.feedProfileImageUrl?.let{
            Glide.with(context)
                .load(data.feedProfileImageUrl)
                .into(holder.feedProfileImage)
        }
        holder.feedHashTag.text = hashTagText
        holder.feedTime.text = data.feedTime
    }
}