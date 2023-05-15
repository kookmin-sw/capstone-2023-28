package com.capstone.traffic.ui.my.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import com.capstone.traffic.databinding.ActivitySettingBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.ui.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class SettingActivity : AppCompatActivity() {
    private val settingViewModel : SettingViewModel by viewModels()
    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setting = settingViewModel


        settingViewModel.alert.observe(this, Observer {
            binding.switchButton.isChecked = it
        })

        binding.switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) requestPermission()
                sub("chul")
            }
            else{
                unSub("chul")
            }
        }
        binding.logoutBtn.setOnClickListener {
            LogoutDialog()
        }
        setContentView(binding.root)
    }

    private fun sub(topic : String){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Toast.makeText(this,"알림 설정 완료", Toast.LENGTH_SHORT).show()
                MyApplication.prefs.setAlert(true)
            }
            else {
                Toast.makeText(this,"알림 설정 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun LogoutDialog() {
        val dialog: AlertDialog = this.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.apply {
                this.setMessage("로그아웃 하시겠습니까?")
                this.setCancelable(false)
                this.setPositiveButton("로그아웃") { dialog, _ ->
                    dialog.dismiss()
                    MyApplication.prefs.setToken(null)
                    MyApplication.prefs.setBoolean("status",false)
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
                this.setNegativeButton("취소") { dialog, _ ->
                    dialog.cancel()
                }
            }
            builder.create()
        }
        dialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                //권한이 허용됐을 때
                override fun onPermissionGranted() {
                }

                //권한이 거부됐을 때
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                }
            })
            .setDeniedMessage("알림 설정을 허용해주세요.")
            .setPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
            .check()
    }

    private fun unSub(topic: String){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener {task ->
            if(task.isSuccessful) {
                Toast.makeText(this,"알림 취소 완료", Toast.LENGTH_SHORT).show()
                MyApplication.prefs.setAlert(false)
            }
            else {
                Toast.makeText(this,"알림 취소 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}