package android.kezdi.ors.Interface

import android.kezdi.ors.Models.Cities
import android.kezdi.ors.Models.Restaurant
import android.kezdi.ors.Models.Restaurants
import retrofit2.Call

interface HTTPSAPI {

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