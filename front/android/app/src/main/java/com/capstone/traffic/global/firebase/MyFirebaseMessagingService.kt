package com.capstone.traffic.global.firebase

 import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.capstone.traffic.R
import com.capstone.traffic.ui.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){

    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token",token).apply()
        editor.apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if(message.data.isNotEmpty()){
            Log.i("바디 : ",message.data["body"].toString())
            Log.i("타이틀 : ",message.data["title"].toString())
            sendNotification(message)
        }
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(message: RemoteMessage){
        val unId : Int = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        var pendingIntent = PendingIntent.getActivity(this,unId,intent,PendingIntent.FLAG_IMMUTABLE)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getActivity(this,unId,intent,PendingIntent.FLAG_ONE_SHOT)
        }
        else {
            pendingIntent = PendingIntent.getActivity(this,unId,intent,PendingIntent.FLAG_IMMUTABLE)
        }

        // 체널이름
        val channelId = "chungeuni"

        // 알림 소리
        val soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // ui
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.data["body"].toString())
            .setContentText(message.data["title"].toString())
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,"Notice",NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(unId,notificationBuilder.build())
    }
}
