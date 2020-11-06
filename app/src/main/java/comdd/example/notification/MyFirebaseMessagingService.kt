package comdd.example.notification

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMsgService"

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        //Displaying token on logcat
        Log.d("MyFirebaseIIDService", "Refreshed token: $s")
        //calling the method store token and passing token
        storeToken(s)
    }

    private fun storeToken(token: String) {
        UserData(applicationContext).writeToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage!!.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())
            try {
                val json = JSONObject(remoteMessage.data.toString())
                sendPushNotification(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }

    private fun sendPushNotification(json: JSONObject) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON $json")
        try {
            //getting the json data
            val data = json.getJSONObject("data")

            //parsing json data
            val title = data.getString("title")
            val message = data.getString("message")
            val imageUrl = data.getString("image")

            //creating MyNotificationManager object
            val mNotificationManager = MyNotificationManager(applicationContext)

            //creating an intent for the notification
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("NotificationMessage", message)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            //if there is no image
            if (imageUrl == "null") {
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent)
            } else {
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

}