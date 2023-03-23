import android.os.Bundle
import android.util.JsonReader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentBusBinding
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.ui.home.direction.transportType.DirectionAdapter
import com.capstone.traffic.ui.home.direction.transportType.DirectionData
import com.capstone.traffic.ui.home.direction.transportType.Serial
import com.capstone.traffic.ui.home.direction.transportType.route.Route
import com.google.gson.JsonSerializer

class BusFragment : Fragment() {
    private val binding by lazy { FragmentBusBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = arguments
        val data = bundle?.getSerializable("data") as Serial
        val directionAdapter = DirectionAdapter(requireContext())

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
                        color = legs.routeColor
                    )
                    routes.add(route)
                }
            }
            directionData.route = routes
            datas.add(directionData)
        }

        directionAdapter.datas = datas

        binding.rv.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            this.adapter = directionAdapter
        }
        directionAdapter.notifyDataSetChanged()


        return binding.root
    }

}