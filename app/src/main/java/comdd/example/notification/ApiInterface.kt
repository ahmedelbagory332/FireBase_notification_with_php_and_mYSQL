package comdd.example.notification

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("RegisterDevice.php")
    fun registerDevice(@Field("email") email:String, @Field("token") token:String) : Call<RegisterDeviceModel>

}