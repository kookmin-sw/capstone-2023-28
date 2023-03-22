import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentBusBinding
import com.capstone.traffic.ui.home.direction.transportType.DirectionAdapter
import com.capstone.traffic.ui.home.direction.transportType.DirectionData
import com.capstone.traffic.ui.home.direction.transportType.route.Route

class BusFragment : Fragment() {
    private val binding by lazy { FragmentBusBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = arguments
        val data = bundle?.getSerializable("data")
        print(data)

        val directionAdapter = DirectionAdapter(requireContext())
        directionAdapter.datas = listOf(
            DirectionData("2000", "10000", listOf(
                Route(
                    type = "Bus",
                    name = "153",
                    station = "길음역",
                    color = ""
                ),
                Route(
                    type = "기차",
                    name = "1호선",
                    station = "노원역",
                    color = ""
                ),
            )),
            DirectionData("4000", "5000",listOf(
                Route(
                    type = "Bus",
                    name = "153",
                    station = "길음역",
                    color = ""
                ),
                Route(
                    type = "기차",
                    name = "1호선",
                    station = "노원역",
                    color = ""
                ),
            ))
        )

        binding.rv.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            this.adapter = directionAdapter
        }

        directionAdapter.notifyDataSetChanged()


        return binding.root
    }

}