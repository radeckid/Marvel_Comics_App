package pl.damrad.marvelcomicsapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MarvelComicsApp)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val controller = Navigation.findNavController(this, R.id.fragment_nav_host)
        binding.bottomNavigationView.setupWithNavController(controller)

        if (auth.currentUser != null) {
            controller.navigate(R.id.action_loginFragment_to_comicsFragment)
        }

        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment, R.id.loginFragment, R.id.registrationFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}