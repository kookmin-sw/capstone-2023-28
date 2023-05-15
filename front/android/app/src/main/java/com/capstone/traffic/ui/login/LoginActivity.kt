package com.capstone.traffic.ui.login

import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityLoginBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.ui.checkout.CheckOutActivity
import com.capstone.traffic.ui.HomeActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override var layoutResourceId: Int = R.layout.activity_login
    private lateinit var loginViewModel : LoginViewModel

    override fun initBinding() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.login = loginViewModel

        // 바로 홈화면으로
        // goHome()

        if(checkLoginStatus()) {
            goHome()
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
    var lastTimeBackPressed : Long = 0
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed >= 1500){
            lastTimeBackPressed = System.currentTimeMillis()
            Toast.makeText(this,"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG).show() }
        else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            System.exit(0)
        }
    }
    private fun goHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun checkLoginStatus() : Boolean {
        val hasToken = MyApplication.prefs.getToken() != null
        return MyApplication.prefs.getBoolean("status") || hasToken
    }
}