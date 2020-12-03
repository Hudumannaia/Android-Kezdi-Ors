package android.kezdi.ors.Networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object WEB {
    var singleton: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://opentable.herokuapp.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    operator fun invoke(): Retrofit {
        return singleton
    }
}