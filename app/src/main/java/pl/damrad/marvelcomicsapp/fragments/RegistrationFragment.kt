package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.FragmentRegistrationBinding
import pl.damrad.marvelcomicsapp.viewmodels.UserViewModel

class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = FragmentRegistrationBinding.inflate(inflater, container, false)
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

        userViewModel.isRepeatedPasswordValid.observe(viewLifecycleOwner) { result ->
            if (!result) binding?.repeatPasswordET?.error = getString(R.string.passwordError)
        }

        binding?.repeatPasswordET?.doOnTextChanged { text, _, _, _ ->
            userViewModel.checkPasswordValid(text)
        }

        userViewModel.userCreateStatus.observe(viewLifecycleOwner) { result ->
            if (result) findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    private fun setOnClickListeners(view: View) {
        binding?.alreadyHaveAccountBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding?.registerBtn?.setOnClickListener {
            val password = binding?.passwordET?.text.toString()
            val password2 = binding?.repeatPasswordET?.text.toString()
            val email = binding?.emailET?.text.toString()
            userViewModel.signUp(email, password, password2, view.context)
        }
    }
}