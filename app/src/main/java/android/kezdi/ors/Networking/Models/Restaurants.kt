package android.kezdi.ors.Networking.Models

import com.google.gson.annotations.SerializedName

data class Restaurants( var total_entries: Int = 0,
                        var per_page: Int  = 25,
                        @SerializedName("current_page", alternate= arrayOf("page"))
                        var current_page: Int = 0,
                        var restaurants: MutableList<Restaurant?> = mutableListOf())
