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
    companion object{
        const val FCM_TAG = "FCM Service"
        const val TAG = "FirebaseService"
    }

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
            Log.d(FCM_TAG, "receive data $warningId")
            var notification : Pair<String,String> = handleNow(warningId)

            sendNotification(notification.first,notification.second,warningId)
        }

        // 알림 메시지 처리
        remoteMessage.notification?.let {
            Log.d(FCM_TAG, "알림 메시지 전송")
            val title = it.title ?:"알림"
            val body = it.body ?: "내용 확인 불가"
            val warningId = remoteMessage.data["warningId"] ?: "0"
            Log.d(FCM_TAG,"title $title body $body 알림 $warningId")
            sendNotification(title,body,warningId)
        }
    }


    private fun handleNow(warningId: String) : Pair<String,String> {
        var title: String = "알 수 없는 경고"
        var body: String = "알 수 없는 경고"
        var pair = Pair("알 수 없는 경고", "알 수 없는 경고")
        when(warningId.toInt()) {
            0 ->{
                title = "정상적으로 작동중"
                body = "정상"
                pair = Pair(title,body)
            }
            1 ->{
                title = "화재 경보기 작동"
                body = "화재 센서 확인 바람"
                pair = Pair(title,body)
            }
            2 ->{
                title = "가스 누출 경보기 작동"
                body = "가스 누출 경보기 확인 바람"
                pair = Pair(title,body)
            }
            3 ->{
                title = "지진 감지기 작동"
                body = "지진 센서 확인 바람"
                pair = Pair(title,body)
            }
            else ->{
                pair = Pair("알 수 없는 경고","알 수 없는 경고")
            }

        }
        return pair
    }

    private fun sendNotification(title: String, body: String,warningId: String) {
        Log.d(TAG,"intent시작")
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("warningId",warningId)
            Log.d(FCM_TAG,"sendNotification$warningId")
        }
        var requestCode = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        val pendingIntent = PendingIntent.getActivity(
            this, requestCode, intent,
            PendingIntent.FLAG_MUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "fcm_default_channel")
            .setSmallIcon(R.drawable.baseline_warning_24)
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