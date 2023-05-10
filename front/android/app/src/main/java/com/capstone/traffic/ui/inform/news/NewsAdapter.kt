package com.capstone.traffic.ui.inform.news

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.DocumentsContract.Document
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.traffic.R
import com.capstone.traffic.model.network.news.NewSet
import com.capstone.traffic.model.network.news.News
import com.capstone.traffic.model.network.news.Res
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import kotlin.concurrent.thread

class NewsAdapter(private val context: Context, private val onClickListener : (NewSet) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    var datas = listOf<NewSet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_news, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<AppCompatImageView>(R.id.image)
        private val title = itemView.findViewById<TextView>(R.id.title)
        private val description = itemView.findViewById<TextView>(R.id.description)

        init {
            itemView.setOnClickListener {
                onClickListener(datas[position])
            }
        }
        fun bind(item : NewSet){

            Glide.with(context)
                .load(item.image)
                .into(image)

            title.text = item.title
            description.text = item.description
        }

        private fun String.stringToBitmap() : Bitmap {
            val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        }



    }

}