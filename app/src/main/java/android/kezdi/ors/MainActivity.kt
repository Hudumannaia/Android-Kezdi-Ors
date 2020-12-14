package android.kezdi.ors

import android.kezdi.ors.Fragments.Home
import android.kezdi.ors.Fragments.Splash
import android.kezdi.ors.Networking.HTTPApiService
import android.kezdi.ors.Networking.Models.APIStats
import android.kezdi.ors.Networking.Models.Cities
import android.kezdi.ors.Networking.Models.Restaurant
import android.kezdi.ors.Networking.Models.Restaurants
import android.kezdi.ors.Networking.WEB
import android.kezdi.ors.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

///TMP https://developer.android.com/codelabs/kotlin-android-training-internet-data#0

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val splash_fragment: Splash = Splash()
    private val home_fragment: Home = Home()
    //private val profile_fragment: Profile = Profile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        //val view = binding.root //Nem tudom kell-e
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, splash_fragment)
            .commit()

        //WEB().create(HTTPApiService::class.java).apiStats().enqueue(apistats)
        //WEB().create(HTTPApiService::class.java).getRestaurant(107257).enqueue(restaurant)
        WEB().create(HTTPApiService::class.java).findRestaurants(city="Chicago",name="steak").enqueue(restaurants)
    }


    private val apistats = object: Callback<APIStats> {
        override fun onFailure(call: Call<APIStats>, t: Throwable) {
            Toast.makeText(baseContext, "APIStats FAIL", Toast.LENGTH_LONG).show()
        }
        override fun onResponse(call: Call<APIStats>, response: Response<APIStats>) {
            Toast.makeText(baseContext, response.body()!!.toString() ,Toast.LENGTH_LONG).show()

            //WEB().create(HTTPApiService::class.java).getCities().enqueue(cities)
        }
    }

    private val cities = object: Callback<Cities> {
        override fun onFailure(call: Call<Cities>, t: Throwable) {
            Toast.makeText(baseContext, "Cities FAIL", Toast.LENGTH_LONG).show()
        }
        override fun onResponse(call: Call<Cities>, response: Response<Cities>) {
            Toast.makeText(baseContext, response.body()!!.toString() ,Toast.LENGTH_LONG).show()
        }
    }

    private val restaurant = object: Callback<Restaurant>{
        override fun onFailure(call: Call<Restaurant>, t: Throwable) {
            Toast.makeText(baseContext, "Restaurant FAIL", Toast.LENGTH_LONG).show()
        }
        override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
            Toast.makeText(baseContext, response.body()!!.toString() ,Toast.LENGTH_LONG).show()
        }
    }

    private val restaurants = object: Callback<Restaurants>{
        override fun onFailure(call: Call<Restaurants>, t: Throwable) {
            Toast.makeText(baseContext, "Restaurants FAIL", Toast.LENGTH_LONG).show()
        }
        override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
            Toast.makeText(baseContext, response.body()!!.toString() ,Toast.LENGTH_LONG).show()
        }
    }

}