package android.kezdi.ors.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.kezdi.ors.R
import android.kezdi.ors.databinding.FragmentSplashBinding
import android.view.animation.AnimationUtils

//https://www.youtube.com/watch?v=JLIFqqnSNmg -- Animation
//https://www.youtube.com/watch?v=WyAzD7RMwHM -- ProgressBar

class Splash : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSplashBinding.inflate(layoutInflater)
        binding.logo.animation = AnimationUtils.loadAnimation(context, R.anim.logo_animation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}

