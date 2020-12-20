package android.kezdi.ors.Fragments

import android.kezdi.ors.Adapter.MyAdapter
import android.kezdi.ors.Interface.ILoadMore
import android.kezdi.ors.MainActivity
import android.kezdi.ors.Models.Restaurant
import android.kezdi.ors.Models.Restaurants
import android.kezdi.ors.API
import android.kezdi.ors.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.kezdi.ors.databinding.FragmentHomeBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//https://www.youtube.com/watch?v=PamhELVWYY0 -- Recycler View + Dynamic Load
//https://youtu.be/9v_7O6kXg2Q -- Kotlin version...


class Home : Fragment(), ILoadMore, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var activity: MainActivity
    lateinit var adapter: MyAdapter
    private var totalEntries: Int = 0
    private var nextPage: String = "1"
    private var oName: String = ""
    private var oCity: String = ""
    private val spinerAllText: String = "All City"
    var cities: MutableList<String> = mutableListOf(spinerAllText)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity = getActivity() as MainActivity

        binding.profile.setOnClickListener { activity.openProfile() }

        binding.searchbar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) this.onQueryTextSubmit("")
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                oName = query?.trim() ?: ""
                API()?.findRestaurants(name = oName, city = oCity)?.enqueue(newSearch)
                return true
            }
        })

        binding.citySpinner.onItemSelectedListener = this
        oCity = if( cities.first() == spinerAllText ) "" else cities.first()
        ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, cities)
            .also { spinnerAdapter ->
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.citySpinner.adapter = spinnerAdapter
            }

        binding.list.layoutManager = LinearLayoutManager(context)   //A sorok sorrendjere vigyazni kell...
        adapter = MyAdapter(binding.list, activity, mutableListOf(Restaurant(name="Egy API sem elérhető.", address="Térjen vissza később!")))
        binding.list.adapter = adapter
        binding.list.setHasFixedSize(true)  //pont ez a sor fogalmom sincs mit csinal, de a tutorialban benne van es nem tunik rossz dolognak
        adapter.setLoadMore(this)

        API()?.findRestaurants(name = oName, city = oCity)?.enqueue(newSearch)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onLoadMore() {
        if(adapter.items.size < totalEntries){
            adapter.items.add(null)
            adapter.notifyItemInserted(adapter.items.size-1)
            API()?.findRestaurants(name = oName, city = oCity, page = nextPage)?.enqueue(loadMore)
        }
    }

    private val newSearch = object: Callback<Restaurants> {
        override fun onFailure(call: Call<Restaurants>, t: Throwable) {}
        override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) =
                when(response.code()){
                    in 200..299 -> {
                        adapter.items = response.body()!!.restaurants
                        totalEntries = response.body()!!.total_entries
                        nextPage = (response.body()!!.current_page+1).toString()

                        adapter.notifyDataSetChanged()
                        adapter.setLoaded()
                    } else -> { }
            }
    }

    private val loadMore = object: Callback<Restaurants> {
        override fun onFailure(call: Call<Restaurants>, t: Throwable) {}
        override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) =
                when(response.code()){
                    in 200..299 -> {
                        adapter.items.removeAt(adapter.items.size - 1)
                        adapter.notifyItemRemoved(adapter.items.size)

                        adapter.items.addAll(response.body()!!.restaurants)
                        totalEntries = response.body()!!.total_entries
                        nextPage = (response.body()!!.current_page+1).toString()

                        adapter.notifyDataSetChanged()
                        adapter.setLoaded()
                    } else -> { }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        oCity = if( position == 0 ) "" else cities[position]
        API()!!.findRestaurants(name = oName, city = oCity).enqueue(newSearch)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        oCity = cities.first()
        API()!!.findRestaurants(name = oName, city = oCity).enqueue(newSearch)
    }

    override fun onResume() {
        super.onResume()
        activity.getUser().also { user ->
            if (user.photoPath.isNotEmpty()) Glide
                    .with(activity)
                    .load(user.photoPath)
                    .placeholder(R.drawable.blank_profile)
                    .centerCrop()
                    .into(binding.profile)
        }
        adapter.notifyDataSetChanged()
    }
}

