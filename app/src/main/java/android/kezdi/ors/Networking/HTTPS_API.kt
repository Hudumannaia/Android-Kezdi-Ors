package android.kezdi.ors.Networking

import android.kezdi.ors.Networking.Models.APIStats
import android.kezdi.ors.Networking.Models.Cities
import android.kezdi.ors.Networking.Models.Restaurant
import android.kezdi.ors.Networking.Models.Restaurants
import retrofit2.Call

interface HTTPS_API {

    fun apiStats(): Call<String>

    fun getCities(): Call<Cities>

    fun findRestaurants(price: String = "",
                        name: String = "",
                        address: String = "",
                        state: String = "",
                        city: String = "",
                        zip: String = "",
                        country: String = "",
                        page: String = "1",
                        per_page: String = "25"
    ): Call<Restaurants>

    fun getRestaurant(id: Int): Call<Restaurant>
}