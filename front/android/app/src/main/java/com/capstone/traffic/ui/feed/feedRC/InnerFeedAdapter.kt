package com.capstone.traffic.ui.feed.feedRC

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Images

class InnerFeedAdapter(private val context: Context) : RecyclerView.Adapter<InnerFeedAdapter.ViewHolder>() {
    var datas = listOf<Images>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerFeedAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_inner_feedview, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: InnerFeedAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<AppCompatImageView>(R.id.imageView)
        init {
        }
        fun bind(item : Images){
            imageView.setBackgroundDrawable(BitmapDrawable(item.image.stringToBitmap()))
        }
    }

    private fun String.stringToBitmap() : Bitmap {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}