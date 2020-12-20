package android.kezdi.ors.Fragments

import android.app.Activity
import android.content.Intent
import android.kezdi.ors.Adapter.MyAdapter
import android.kezdi.ors.MainActivity
import android.kezdi.ors.Models.Restaurant
import android.kezdi.ors.Models.User
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.kezdi.ors.databinding.FragmentProfileBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

class Profile : Fragment(), View.OnFocusChangeListener {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var activity: MainActivity
    lateinit var adapter: MyAdapter
    private var user: User = User()
    private val IMAGE_PICK: Int = 9071

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        activity = getActivity() as MainActivity

        binding.backButton.setOnClickListener { activity.onBackPressed() }

        user = activity.getUser()

        if(user.photoPath.isNotEmpty()) Glide
                .with(activity)
                .load(user.photoPath)
                .centerCrop()
                .into(binding.profilePic)
        binding.profilePic.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK)
        }

        binding.userName.onFocusChangeListener = this
        binding.userName.setText(user.name)
        binding.userEmail.onFocusChangeListener = this
        binding.userEmail.setText(user.email)
        binding.userAddress.onFocusChangeListener = this
        binding.userAddress.setText(user.address)
        binding.userPhoneNumber.onFocusChangeListener = this
        binding.userPhoneNumber.setText(user.phoneNumber)

        binding.favourites.layoutManager = LinearLayoutManager(context)
        adapter = MyAdapter(binding.favourites, activity, activity.getAllFavourites().toMutableList())
        binding.favourites.adapter = adapter
        binding.favourites.setHasFixedSize(true)  //pont ez a sor fogalmom sincs mit csinal, de a tutorialban benne van es nem tunik rossz dolognak

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onFocusChange(v: View?, hasFocus: Boolean) { if (!hasFocus) updateUserData() }

    fun updateUserData() {
        user.name = binding.userName.text.toString()
        user.email = binding.userEmail.text.toString()
        user.address = binding.userAddress.text.toString()
        user.phoneNumber = binding.userPhoneNumber.text.toString()
        activity.updateUser(user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if( resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK ){
            user.photoPath = data?.data.toString()
            if(user.photoPath.isNotEmpty()) Glide
                    .with(activity)
                    .load(user.photoPath)
                    .centerCrop()
                    .into(binding.profilePic)
            updateUserData()
        }
    }
}