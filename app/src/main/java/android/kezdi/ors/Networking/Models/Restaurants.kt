package android.kezdi.ors.Networking.Models

import java.util.ArrayList

data class Restaurants( var total_entries: Int,
                        var per_page: Int,
                        var current_page: Int,
                        var restaurants: ArrayList<Restaurant> = ArrayList())
