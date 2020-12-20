package android.kezdi.ors

import android.kezdi.ors.Interface.*
import android.os.Debug
import android.util.Log
import android.widget.Toast
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Timeout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object API {
    operator fun invoke() : HTTPSAPI? = apis.firstOrNull()

    fun tryBackup(): HTTPSAPI? {
        apis.removeFirstOrNull()
        return this()
    }

    private val client : OkHttpClient = OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()

    private var apis: MutableList<HTTPSAPI> = mutableListOf(
            Retrofit
                    .Builder()
                    .baseUrl("http://62.121.98.198:25585")  //hosted by Ors
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(client)
                    .build()
                    .create(HomeAPI::class.java),
            Retrofit
                    .Builder()
                    //.baseUrl("http://orsapi.byethost18.com")
                    .baseUrl("http://orsapi.000webhostapp.com") //hosted by Mirtill
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(client)
                    .build()
                    .create(OrsAPI::class.java),
            Retrofit
                    .Builder()
                    .baseUrl("https://opentable.herokuapp.com")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OpentableAPI::class.java),
            Retrofit
                    .Builder()
                    .baseUrl("https://ratpark-api.imok.space")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RatparkAPI::class.java)
    )
}