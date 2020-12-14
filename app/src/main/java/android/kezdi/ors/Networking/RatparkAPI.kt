package android.kezdi.ors.Networking

import android.kezdi.ors.Networking.Models.APIStats
import android.kezdi.ors.Networking.Models.Cities
import android.kezdi.ors.Networking.Models.Restaurant
import android.kezdi.ors.Networking.Models.Restaurants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface RatparkAPI : HTTPS_API {

    @GET("/")
    override fun apiStats(): Call<String>

    @GET("/cities")
    override fun getCities(): Call<Cities>

    @GET("/restaurants")
    override fun findRestaurants(@Query("price") price: String,
                                 @Query("name") name: String,
                                 @Query("address") address: String,
                                 @Query("state") state: String,
                                 @Query("city") city: String,
                                 @Query("zip") zip: String,
                                 @Query("country") country: String,
                                 @Query("page") page: String,
                                 @Query("per_page") per_page: String
    ): Call<Restaurants>

    @GET("/restaurants/{id}")
    override fun getRestaurant(@Path("id") id: Int): Call<Restaurant>

}