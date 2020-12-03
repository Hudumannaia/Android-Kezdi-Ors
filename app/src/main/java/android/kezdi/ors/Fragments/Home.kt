package android.kezdi.ors.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.kezdi.ors.databinding.FragmentHomeBinding
import android.kezdi.ors.databinding.FragmentSplashBinding

//https://www.youtube.com/watch?v=PamhELVWYY0 -- Recycler View + Dynamic Load

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity
        return binding.root
    }

}