package com.capstone.traffic.ui.home.direction.transportType

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Path.Direction
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R

class DirectionAdapter(private val context: Context) : RecyclerView.Adapter<DirectionAdapter.ViewHolder>(){
    var datas = listOf<DirectionData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.direction_recycler_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hour = itemView.findViewById<TextView>(R.id.hour)
        var minuate = itemView.findViewById<TextView>(R.id.minuate)
        val price = itemView.findViewById<TextView>(R.id.price_tv)
        val visibleBtn = itemView.findViewById<AppCompatButton>(R.id.visible_btn)
        val detailLl = itemView.findViewById<LinearLayout>(R.id.detail_ll)
        init {
            visibleBtn.setOnClickListener {
                if(detailLl.visibility == View.VISIBLE) {
                    visibleBtn.setBackgroundResource(R.drawable.icon_keyboard_arrow_up)
                    detailLl.visibility = View.GONE
                }
                else{
                    visibleBtn.setBackgroundResource(R.drawable.icon_keyboard_arrow_down)
                    detailLl.visibility = View.VISIBLE
                }
            }
        }
        fun bind(item: DirectionData) {
            val mint : Int = item.time.toInt() / 60
            val hr : Int = mint / 60
            hour.text = hr.toString()
            minuate.text = mint.toString()
            price.text = item.price
        }
    }
}