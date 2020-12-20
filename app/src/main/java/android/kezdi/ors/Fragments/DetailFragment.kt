package android.kezdi.ors.Fragments

import android.annotation.SuppressLint
import android.kezdi.ors.MainActivity
import android.kezdi.ors.Models.Restaurant
import android.kezdi.ors.API
import android.kezdi.ors.databinding.FragmentDetailBinding
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment(private val rId: Int) : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDetailBinding

    private lateinit var activity: MainActivity
    private val mapViewBundleKey: String = "map $rId"
    private var location = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailBinding.inflate(layoutInflater)
        activity = getActivity() as MainActivity

        API()!!.getRestaurant(rId).enqueue(restaurant)

        binding.backButton.setOnClickListener { activity.onBackPressed() }

        binding.mapView.onCreate(savedInstanceState?.getBundle(mapViewBundleKey))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    private val restaurant = object: Callback<Restaurant> {
        @SuppressLint("SetTextI18n")
        override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) = when(response.code()){
            in 200..299 -> {
                val restaurant = response.body()!!
                Glide
                    .with(activity)
                    .load(restaurant.image_url)
                    .centerCrop()
                    .into(binding.rPic)
                binding.rPrice.text = "$".repeat(restaurant.price)
                binding.rName.text = restaurant.name
                binding.rAddressCity.text = restaurant.address + ", " + restaurant.city
                binding.rCountryStatePostalCode.text = "${restaurant.country}/${restaurant.state} ${restaurant.postal_code}"
                binding.rPhone.text = restaurant.phone
                location = LatLng(restaurant.lat.toDouble(), restaurant.lng.toDouble())
                binding.mapView.getMapAsync(this@DetailFragment)
            }
            else -> activity.onBackPressed()
        }
        override fun onFailure(call: Call<Restaurant>, t: Throwable) = activity.onBackPressed()
    }

    override fun onMapReady(mMap: GoogleMap?) {
        mMap?.addMarker(MarkerOptions().position(location).title("POS"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState.getBundle(mapViewBundleKey)?:Bundle()){
            outState.putBundle(mapViewBundleKey, this)
            binding.mapView.onSaveInstanceState(this)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}