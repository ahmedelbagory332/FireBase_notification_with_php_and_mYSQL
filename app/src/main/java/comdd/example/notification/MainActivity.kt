package comdd.example.notification

 import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

     lateinit var buttonRegister: Button
     lateinit var editTextEmail: EditText
     @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         onNewIntent(intent)

    }


    /*

    this method to get the data from notification after we put it in the intent in MyFirebaseMessagingService class
    and we must put this tag "  android:launchMode="singleTop"  " in manifests in activity which will show the data

     */

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setContentView(R.layout.activity_main)
        editTextEmail = findViewById(R.id.editTextEmail)
        buttonRegister = findViewById(R.id.buttonRegister)

        val extras = intent!!.extras
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {
                // extract the extra-data in the Notification
                val msg = extras.getString("NotificationMessage")
                editTextEmail.setText(msg)
            }
        }





        //adding listener to view
        buttonRegister.setOnClickListener {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
                val newToken = instanceIdResult.token

                UserData(applicationContext).writeToken(newToken)

                ApiClient().getINSTANCE()?.registerDevice(editTextEmail.text.toString(),newToken)?.enqueue(object :
                    Callback<RegisterDeviceModel> {
                    override fun onResponse(call: Call<RegisterDeviceModel>, response: Response<RegisterDeviceModel>) {

                        Toast.makeText(applicationContext,response.body()!!.message,Toast.LENGTH_LONG).show()

                    }

                    override fun onFailure(call: Call<RegisterDeviceModel>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_LONG).show()
                    }
                })
            }




        }

    }
}