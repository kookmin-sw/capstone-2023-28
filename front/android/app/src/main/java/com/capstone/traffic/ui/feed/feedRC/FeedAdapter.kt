package com.capstone.traffic.ui.feed.feedRC

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.capstone.traffic.R
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Res
import com.capstone.traffic.ui.feed.comment.CommentsActivity
import java.sql.Time

class FeedAdapter(private val context: Context, private val onClickListener : (Res) -> Unit) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var datas = listOf<Res>()

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
        private val innerRc = itemView.findViewById<RecyclerView>(R.id.inner_rc)
        private val profileIv = itemView.findViewById<AppCompatImageView>(R.id.profile_IV)
        private val commentBtn = itemView.findViewById<AppCompatButton>(R.id.comment_btn)

        init {
        }
        @SuppressLint("NotifyDataSetChanged")
        fun bind(item : Res){
            userNickname.text = item.user?.userNickname ?: ""
            writeTime.text = item.createdAt.parseTime()
            contents.text = item.content
            commentBtn.apply {
                this.setOnClickListener {
                    onClickListener(datas[position])
                }
                this.text = item.comments
            }

            profileIv.apply {
                if(item.user?.userProfile != null) setBackgroundDrawable(BitmapDrawable(item.user?.userProfile.stringToBitmap()))
            }


            if(item.images != null)
            {
                val adapter = InnerFeedAdapter(context)
                adapter.datas = item.images
                innerRc.apply {
                    this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    this.adapter = adapter
                }
                adapter.notifyDataSetChanged()
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
}