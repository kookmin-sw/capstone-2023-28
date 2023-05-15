package com.capstone.traffic.ui.feed.find

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.databinding.ActivityFindBinding
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import com.capstone.traffic.model.network.sql.dataclass.info.Res
import com.capstone.traffic.ui.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FindActivity : AppCompatActivity() {
    private val viewModel: FindViewModel by viewModels()
    private val binding by lazy { ActivityFindBinding.inflate(layoutInflater) }
    private var service = Client.getInstance().create(Service::class.java)
    private var userData = listOf<Res>()
    lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userAdapter = UserAdapter(this,
        userCLickEvent = {
                val profileIntent = Intent(this, ProfileActivity::class.java)
                profileIntent.putExtra("userName",it.userNickName)
                startActivity(profileIntent)
            }
        )

        setInfoRecyclerView()

        binding.apply {
            // 유저 검색 결과 리사이클러 뷰
            findSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText != null && newText.isNotBlank()) getInfo(newText)
                    else if(newText == null || newText.isBlank()) {
                        userData = listOf()
                        setInfoRecyclerView()
                    }
                    return false
                }
            })
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setInfoRecyclerView()
    {
        userAdapter.apply {
            datas = userData
        }

        binding.infoRc.apply {
            this.layoutManager = LinearLayoutManager(this@FindActivity, RecyclerView.VERTICAL,false)
            this.adapter = userAdapter
        }
        userAdapter.notifyDataSetChanged()
    }

    private fun getInfo(nickname : String){
        service.getInfo(userNickname = nickname, userEmail = null, pageNum = "5").enqueue(object : Callback<InfoRecSuc>{
            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {

            }

            override fun onResponse(call: Call<InfoRecSuc>, response: Response<InfoRecSuc>) {
                if(response.isSuccessful) {
                    if(response.body()?.res!!.isNotEmpty()) {
                        userData = response.body()!!.res
                        setInfoRecyclerView()
                    }
                }
            }
        })
    }

}