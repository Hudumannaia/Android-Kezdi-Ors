package android.kezdi.ors

import android.graphics.drawable.Drawable
import android.kezdi.ors.Fragments.*
import android.kezdi.ors.Models.*
import android.kezdi.ors.Repository.DBHelper
import android.kezdi.ors.databinding.ActivityMainBinding
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
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
    private lateinit var db: DBHelper
    private var currentUserId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, splash_fragment)
            .commit()

        db = DBHelper(this)

        API()!!.apiStats().enqueue(apiStats)

        Handler().postDelayed({ //Ha tul sokat a Splash-en van/ott ragadt tovabb tobja a Homer-a
            if (supportFragmentManager.fragments.last() == splash_fragment)
                loadHomeWithDelay()
        },35000)
    }

    private fun loadHomeWithDelay() {
        Handler().postDelayed({
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentContainer.id, home_fragment)
                .commit()
        },1500)
    }

    private val apiStats = object: Callback<String> {   //Lekezeli az N-edik API allapotat
        override fun onResponse(call: Call<String>, response: Response<String>) =
            when(response.code()){
                in 200..299 -> API()!!.getCities().enqueue(cities)
                else -> API.tryBackup()?.apiStats()?.enqueue(this) ?: loadHomeWithDelay()
            }
        override fun onFailure(call: Call<String>, t: Throwable) = API.tryBackup()?.apiStats()?.enqueue(this) ?: loadHomeWithDelay()
    }

    private val cities = object: Callback<Cities> {
        override fun onResponse(call: Call<Cities>, response: Response<Cities>){
            if(response.code() in 200..299)
                home_fragment.cities.addAll(response.body()!!.cities)
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

    fun getAllFavourites(): MutableList<Restaurant>{
        val res: MutableList<Restaurant> = mutableListOf()
        for( fav in db.allFavourites){
            res.add(Restaurant(id=fav.id,name=fav.name,address=fav.address,price=fav.price,image_url=fav.image_url))
        }
        return res
    }

    fun setFavouriteIndicator(id: Int, star: ImageView) =
        if(db.isFavourite(id)) star.setImageResource(android.R.drawable.btn_star_big_on)
        else star.setImageResource(android.R.drawable.btn_star_big_off)

    fun clickFavourite(r: Restaurant, star: ImageView){
        if( db.isFavourite(r.id) ){
            db.deleteFavourite(r.id)
            star.setImageResource(android.R.drawable.btn_star_big_off)
        }else{
            val fav = Favourite(id=r.id, name=r.name,address=r.address,price=r.price,image_url=r.image_url)
            db.addFavourite(fav)
            star.setImageResource(android.R.drawable.btn_star_big_on)
        }
    }

    fun getUser(): User = db.getUser(currentUserId)

    fun updateUser(user: User) {
        user.id = currentUserId
        db.updateUser(user)
    }

}