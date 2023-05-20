package com.capstone.traffic.ui.route.direction.transportType

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.databinding.FragmentBusBinding
import com.capstone.traffic.ui.route.direction.DirectionViewModel
import com.capstone.traffic.ui.route.direction.transportType.route.Route

class BusAndSubwayFragment : Fragment() {
    private val binding by lazy { FragmentBusBinding.inflate(layoutInflater) }

    lateinit var directionAdapter : DirectionAdapter
    lateinit var data: Serial
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 뷰모델
        val viewModel = ViewModelProvider(activity as ViewModelStoreOwner)[DirectionViewModel::class.java]

        val bundle = arguments
        data = bundle?.getSerializable("data") as Serial
        directionAdapter = DirectionAdapter(requireContext())

        viewModel.status.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,it.toString(), Toast.LENGTH_SHORT).show()
            setDirectionRcView(it)
            directionAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun setDirectionRcView(status : Boolean)
    {
        val datas = mutableListOf<DirectionData>()

        data.ser?.forEach {
            val directionData = DirectionData(it.totalTime, it.fare.regular.totalFare, listOf())
            val routes = mutableListOf<Route>()
            it.legs.forEach {legs ->
                if(legs.mode == "BUS" || legs.mode == "SUBWAY"){
                    val route = Route(
                        type = legs.mode,
                        name = legs.route,
                        station = legs.start.name,
                        color = legs.routeColor,
                    )
                    routes.add(route)
                }
            }
            directionData.route = routes
            datas.add(directionData)
        }
        datas.sortBy { if(status) it.time else it.price }
        directionAdapter.datas = datas


        binding.rv.apply {
            this.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            this.adapter = directionAdapter
        }
    }
}