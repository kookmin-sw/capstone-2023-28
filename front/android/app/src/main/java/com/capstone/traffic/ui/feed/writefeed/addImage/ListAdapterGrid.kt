package com.capstone.traffic.ui.feed.writefeed.addImage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.request.RequestOptions
import com.capstone.traffic.R
import kotlinx.coroutines.NonDisposableHandle.parent
import java.io.File

data class Status(
    val position : Int,
    val isDone : Boolean = false
)
class ListAdapterGrid(var items: ArrayList<Image>, val onClickDeleteIcon : (status: Status) -> Unit): RecyclerView.Adapter<ListAdapterGrid.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_image_grid_item, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val deleteBtn = itemView.findViewById<AppCompatButton>(R.id.delete_btn)
        val imageView = itemView.findViewById<AppCompatImageView>(R.id.image)
        init {
            deleteBtn.setOnClickListener {
                onClickDeleteIcon.invoke(Status(position))
            }
        }
        fun bind(image : Image){
            image.let{
                Glide.with(itemView)
                    .load(it.uri)
                    .centerCrop()
                    .into(imageView)
            }
        }
    }
}