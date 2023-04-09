package com.capstone.traffic.ui.login

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityLoginBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.ui.checkout.CheckOutActivity
import com.capstone.traffic.ui.home.HomeActivity

class loginActivity : BaseActivity<ActivityLoginBinding>() {
    override var layoutResourceId: Int = R.layout.activity_login
    private lateinit var loginViewModel : LoginViewModel

    override fun initBinding() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.login = loginViewModel

        // 바로 홈화면으로
        //goHome()

        if(checkLoginStatus()) {
            //goHome()
        }

        binding.createAccountButton.setOnClickListener {
            val intent = Intent(this, CheckOutActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.loginStatus.observe(this, Observer {
            if(it == true){
                goHome()
            }
        })
    }

    fun goHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun checkLoginStatus() : Boolean {
        return MyApplication.prefs.getBoolean("status")
    }
}