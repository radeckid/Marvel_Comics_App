package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {

        binding.loginEmailPassoword.setOnClickListener {
            val password = binding.passwordET.text.toString()
            val email = binding.emailET.text.toString()

            when {
                !isValidEmail(email) -> {
                    binding.emailET.error = getString(R.string.emailError)
                }
                !isValidPassword(password) -> {
                    binding.passwordET.error = getString(R.string.passwordError)
                }
                else -> {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            findNavController().navigate(R.id.action_loginFragment_to_comicsFragment)
                        } else {
                            Toast.makeText(
                                context, getString(R.string.authError),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

private fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
}

private fun isValidPassword(target: CharSequence?): Boolean {
    val regex = Regex("^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$")
    return !TextUtils.isEmpty(target) && regex.matches(target!!)
}