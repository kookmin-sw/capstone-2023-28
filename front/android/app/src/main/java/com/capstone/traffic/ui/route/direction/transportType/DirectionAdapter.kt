package com.capstone.traffic.ui.route.direction.transportType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.ui.route.direction.transportType.route.RouteAdapter

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
        val detailRv = itemView.findViewById<RecyclerView>(R.id.detail_rv)
        init {
            visibleBtn.setOnClickListener {
                if(detailRv.visibility == View.VISIBLE) {
                    visibleBtn.setBackgroundResource(R.drawable.icon_keyboard_arrow_up)
                    detailRv.visibility = View.GONE
                }
                else{
                    visibleBtn.setBackgroundResource(R.drawable.icon_keyboard_arrow_down)
                    detailRv.visibility = View.VISIBLE
                }
            }
        }
        fun bind(item: DirectionData) {
            val adapter = RouteAdapter(context)
            adapter.datas = item.route
            detailRv.apply {
                this.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
                this.adapter = adapter
            }

            val mint : Int = item.time.toInt() / 60
            val ma : Int = mint % 60
            val hr : Int = mint / 60
            hour.text = hr.toString()
            minuate.text = ma.toString()
            price.text = item.price
        }
    }
}