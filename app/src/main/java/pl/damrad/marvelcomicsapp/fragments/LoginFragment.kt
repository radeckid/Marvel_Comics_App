package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.FragmentLoginBinding
import pl.damrad.marvelcomicsapp.viewmodels.UserViewModel

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = FragmentLoginBinding.inflate(inflater, container, false)
        binding = bind
        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners(view)
        setObservers()
    }

    private fun setObservers() {
        userViewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                userViewModel.onToastShown()
            }
        }

        userViewModel.isEmailValid.observe(viewLifecycleOwner) { result ->
            if (!result) binding?.emailET?.error = getString(R.string.emailError)
        }

        binding?.emailET?.doOnTextChanged { text, _, _, _ ->
            userViewModel.checkEmailValid(text)
        }

        userViewModel.isPasswordValid.observe(viewLifecycleOwner) { result ->
            if (!result) binding?.passwordET?.error = getString(R.string.passwordError)
        }

        binding?.passwordET?.doOnTextChanged { text, _, _, _ ->
            userViewModel.checkPasswordValid(text)
        }

        userViewModel.authStatus.observe(viewLifecycleOwner)  { result ->
            if(result) findNavController().navigate(R.id.action_loginFragment_to_comicsFragment)
        }

    }

    private fun setOnClickListeners(view: View) {

        binding?.loginEmailPassoword?.setOnClickListener {
            val password = binding?.passwordET?.text.toString()
            val email = binding?.emailET?.text.toString()
            userViewModel.signIn(email, password, view.context.applicationContext)
        }
    }
}