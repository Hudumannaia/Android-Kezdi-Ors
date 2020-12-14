package android.kezdi.ors.Networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object WEB {
    private var activeAPI_id: Int = 1
    private val opentable: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://opentable.herokuapp.com")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val ratpark: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://ratpark-api.imok.space")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun tryBackup(): HTTPS_API {
        activeAPI_id++
        return invoke()
    }

    operator fun invoke() : HTTPS_API = when(activeAPI_id){
        1 -> opentable.create(OpentableAPI::class.java)
        else -> ratpark.create(RatparkAPI::class.java)
    }
}