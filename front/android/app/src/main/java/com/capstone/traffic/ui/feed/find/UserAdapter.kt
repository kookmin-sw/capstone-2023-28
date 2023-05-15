package com.capstone.traffic.ui.feed.find

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.capstone.traffic.R
import com.capstone.traffic.model.network.sql.dataclass.info.Res

class UserAdapter(private val context: Context, private val userCLickEvent : (Res) -> Unit) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    var datas = listOf<Res>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_search_user_rc, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val profileIv = itemView.findViewById<AppCompatImageView>(R.id.profile_iv)
        private val nicknameTv = itemView.findViewById<TextView>(R.id.nickname_tv)
        private val informTv = itemView.findViewById<TextView>(R.id.inform_tv)
        init {
            itemView.setOnClickListener {
                userCLickEvent(datas[position])
            }
        }
        fun bind(item : Res){
            profileIv.apply {
                setBackgroundDrawable(BitmapDrawable(item.user_profile_image?.stringToBitmap()))
            }
            nicknameTv.text = item.userNickName
            informTv.text = item.userDefinition
        }

        private fun String.stringToBitmap() : Bitmap {
            val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        }
    }
}