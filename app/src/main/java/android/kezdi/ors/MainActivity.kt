package android.kezdi.ors

import android.kezdi.ors.Fragments.*
import android.kezdi.ors.Networking.Models.*
import android.kezdi.ors.Networking.WEB
import android.kezdi.ors.databinding.ActivityMainBinding
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

///TMP https://developer.android.com/codelabs/kotlin-android-training-internet-data#0

/**
 * Nettel szorakozni nagyon necces az API 29 ota, ezert ki is hagytam
 * https://medium.com/swlh/how-to-check-internet-connection-on-android-q-ea7c5a103e3
*/

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val splash_fragment: Splash = Splash()
    private val home_fragment: Home = Home()
    private val profile_fragment: Profile = Profile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, splash_fragment)
            .commit()

        WEB().apiStats().enqueue(opentable)

        Handler().postDelayed({ //Ha tul sokat a Splash-en van/ott ragadt tovabb tobja a Homer-a
            if (supportFragmentManager.fragments.last() == splash_fragment){
                Toast.makeText(baseContext, "Váratlan hiba..." ,Toast.LENGTH_LONG).show()
                supportFragmentManager
                    .beginTransaction()
                    .replace(binding.fragmentContainer.id, home_fragment)
                    .commit()
            }
        },15000)
    }

    private fun loadHomeWithDelay() {
        Handler().postDelayed({
            supportFragmentManager
                    .beginTransaction()
                    .replace(binding.fragmentContainer.id, home_fragment)
                    .commit()
        },1500)
    }

    private val opentable = object: Callback<String> {    //Szándékosan túltoltam a KOTLIN-kodást
        override fun onResponse(call: Call<String>, response: Response<String>) = when(response.code()){
            in 200..299 -> WEB().getCities().enqueue(cities)
            else -> WEB.tryBackup().apiStats().enqueue(ratpark)
        }
        override fun onFailure(call: Call<String>, t: Throwable) = WEB.tryBackup().apiStats().enqueue(ratpark)
    }

    private val ratpark = object: Callback<String> {
        override fun onResponse(call: Call<String>, response: Response<String>) = when (response.code()) {
            in 200..299 ->WEB().getCities().enqueue(cities)
            else -> loadHomeWithDelay()
        }
        override fun onFailure(call: Call<String>, t: Throwable) = loadHomeWithDelay()
    }

    private val cities = object: Callback<Cities> {
        override fun onResponse(call: Call<Cities>, response: Response<Cities>){
            if(response.code() in 200..299) home_fragment.cities.addAll(response.body()!!.cities)
            loadHomeWithDelay()
        }
        override fun onFailure(call: Call<Cities>, t: Throwable) = loadHomeWithDelay()
    }
    fun showDetails(id: Int) = supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(binding.fragmentContainer.id, DetailFragment(id) )
        .commit()

    fun openProfile() = supportFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(binding.fragmentContainer.id, profile_fragment)
        .commit()

}