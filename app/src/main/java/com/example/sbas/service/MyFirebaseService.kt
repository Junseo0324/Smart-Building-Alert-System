package com.example.sbas.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sbas.MainActivity
import com.example.sbas.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {
    private val TAG = "FCM Service"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("토큰","fcm token $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("onMessage", remoteMessage.data.toString())
        // 데이터 메시지 처리
        if(remoteMessage.data.isNotEmpty()){
            val warningId = remoteMessage.data["warningId"] ?: ""
            Log.d(TAG, "receive data $warningId")
            handleNow(warningId)
        }

        // 알림 메시지 처리
        remoteMessage.notification?.let {
            Log.d(TAG, "알림 메시지 전송")
            val title = it.title ?:"알림"
            val body = it.body ?: "내용 확인 불가"
            val warningId = remoteMessage.data["warningId"] ?: "0"
            Log.d(TAG,"title $title body $body 알림 $warningId")
            sendNotification(title,body,warningId)
        }
    }


    private fun handleNow(warningId: String) {

    }

    private fun sendNotification(title: String, body: String,warningId: String) {
        Log.d("sendNotification 시작","intent시작")
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("warningId",warningId)
            Log.d(TAG,"sendNotification$warningId")
        }
        var requestCode = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        val pendingIntent = PendingIntent.getActivity(
            this, requestCode, intent,
            PendingIntent.FLAG_MUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "fcm_default_channel")
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 이상에서 알림을 제공하려면 앱의 알림 채널을 시스템에 등록해야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "fcm_default_channel",
                "SBAS",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }


}