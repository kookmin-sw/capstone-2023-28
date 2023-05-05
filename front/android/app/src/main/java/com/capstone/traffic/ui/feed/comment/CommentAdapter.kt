package com.capstone.traffic.ui.feed.comment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.model.network.sql.dataclass.comment.Res

class CommentAdapter(private val context: Context) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    var datas = listOf<Res>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_recy_comment, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item : Res)
        {
            itemView.apply {
                findViewById<AppCompatImageView>(R.id.image).apply {
                    if(item.user.userProfileImage != null) setBackgroundDrawable(BitmapDrawable(item.user.userProfileImage.stringToBitmap()))
                }
                findViewById<TextView>(R.id.nickName_tv).text = item.user.userNickname
                findViewById<TextView>(R.id.time_tv).text = item.createdAt.parseTime()
                findViewById<TextView>(R.id.contents).text = item.content
            }
        }
    }

    private fun String.parseTime() : String {
        val splitTime = this.split("T")
        if(splitTime.size == 2){
            val t = splitTime[1].split(".")
            return "${splitTime[0]} ${t[0]}"
        }
        return this
    }
    private fun String.stringToBitmap() : Bitmap {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}