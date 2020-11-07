package comdd.example.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MyNotificationManager(private val mCtx: Context) {
    private var NOTIFICATION_CHANNEL_ID = "Bego_channel"


    var rand: Random = Random()
    var ID_BIG_NOTIFICATION = rand.nextInt(1000000000)
    var ID_SMALL_NOTIFICATION = rand.nextInt(1000000000)
    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    @RequiresApi(Build.VERSION_CODES.N)
    fun showBigNotification(title: String?, message: String?, url: String, intent: Intent?) {



        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationChannel.vibrationPattern = longArrayOf(0, 250, 250, 250)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }
        val resultPendingIntent = PendingIntent.getActivity(mCtx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            bigPictureStyle.setSummaryText(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY).toString())
        }
        else {
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        }
        bigPictureStyle.bigPicture(getBitmapFromURL(url))
        val mBuilder =
            NotificationCompat.Builder(mCtx, NOTIFICATION_CHANNEL_ID)
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(title)
            .setStyle(bigPictureStyle)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.resources, R.mipmap.ic_launcher))
            .setContentText(message)
            .setVibrate(longArrayOf(0, 250, 250, 250))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(
            ID_BIG_NOTIFICATION,
            notification
        )
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    fun showSmallNotification(title: String?, message: String?, intent: Intent?) {
        val notificationManager =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 250, 250, 250)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }
        val resultPendingIntent = PendingIntent.getActivity(
            mCtx,
            ID_SMALL_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder =
            NotificationCompat.Builder(mCtx, NOTIFICATION_CHANNEL_ID)
        val notification: Notification
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLargeIcon(BitmapFactory.decodeResource(mCtx.resources, R.mipmap.ic_launcher))
            .setContentText(message)
            .setVibrate(longArrayOf(0, 250, 250, 250))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(
            ID_SMALL_NOTIFICATION,
            notification
        )
    }

    //The method will return Bitmap from an image URL
    private fun getBitmapFromURL(strURL: String): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}