package comdd.example.notification


 import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private val BASE_URL = "https://bego288.000webhostapp.com/php/"
    lateinit var  apiInterface: ApiInterface
    private var INSTANCE: ApiClient? = null

    init {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .client(okHttpClient)
            .build()
        apiInterface = retrofit.create(ApiInterface::class.java)
    }

    fun getINSTANCE(): ApiClient? {
        if (null == INSTANCE) {
            INSTANCE = ApiClient()
        }
        return INSTANCE
    }

   fun registerDevice( email:String,  token:String) : Call<RegisterDeviceModel> = apiInterface.registerDevice(email, token)

}