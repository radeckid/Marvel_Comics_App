package pl.damrad.marvelcomicsapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.ActivityMainBinding
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val controller = Navigation.findNavController(this, R.id.fragment_nav_host)
        binding.bottomNavigationView.setupWithNavController(controller)

        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}