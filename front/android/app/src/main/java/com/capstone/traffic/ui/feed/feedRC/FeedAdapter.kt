package com.capstone.traffic.ui.feed.feedRC

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

class FeedAdapter(private val context: Context) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var datas = listOf<Feed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_feed, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNickname = itemView.findViewById<TextView>(R.id.userNickname)
        private val writeTime = itemView.findViewById<TextView>(R.id.writeTime)
        private val contents = itemView.findViewById<TextView>(R.id.contents)
        init {
        }
        fun bind(item : Feed){
            userNickname.text = item.name
            writeTime.text = item.time
            contents.text = item.content
        }
    }
}