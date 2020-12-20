package android.kezdi.ors.Interface

import android.kezdi.ors.Models.Cities
import android.kezdi.ors.Models.Restaurant
import android.kezdi.ors.Models.Restaurants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OrsAPI : HTTPSAPI {

    @GET("/index.php?")
    override fun apiStats(): Call<String>

    @GET("/cities.php")
    override fun getCities(): Call<Cities>

    @GET("/restaurants.php")
    override fun findRestaurants(@Query("price") price: String,
                                 @Query("srcfor") name: String,
                                 @Query("address") address: String,
                                 @Query("state") state: String,
                                 @Query("city") city: String,
                                 @Query("zip") zip: String,
                                 @Query("country") country: String,
                                 @Query("page") page: String,
                                 @Query("per_page") per_page: String
    ): Call<Restaurants>

    @GET("/restaurants.php")
    override fun getRestaurant(@Query("id") id: Int): Call<Restaurant>
}