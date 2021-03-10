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
import pl.damrad.marvelcomicsapp.other.UIState
import pl.damrad.marvelcomicsapp.viewmodels.UserViewModel

class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = FragmentRegistrationBinding.inflate(inflater, container, false).apply {
            fragment = this@RegistrationFragment
        }
        binding = bind
        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        userViewModel.isEmailValid.observe(viewLifecycleOwner) { result ->
            if (!result) {
                binding?.emailTextField?.error = getString(R.string.emailError)
            } else {
                binding?.emailTextField?.error = null
            }
        }

        binding?.emailET?.doOnTextChanged { text, _, _, _ ->
            userViewModel.checkEmailValid(text)
        }

        userViewModel.isPasswordValid.observe(viewLifecycleOwner) { result ->
            if (!result) {
                binding?.passwordTextField?.error = getString(R.string.passwordError)
            } else {
                binding?.passwordTextField?.error = null
            }
        }

        binding?.passwordET?.doOnTextChanged { text, _, _, _ ->
            userViewModel.checkPasswordValid(text)
        }

        userViewModel.isRepeatedPasswordValid.observe(viewLifecycleOwner) { result ->
            if (!result) {
                binding?.repeatPasswordTextField?.error = getString(R.string.passwordError)
            } else {
                binding?.repeatPasswordTextField?.error = null
            }
        }

        binding?.repeatPasswordET?.doOnTextChanged { text, _, _, _ ->
            userViewModel.checkRepeatedPasswordValid(text)
        }

        userViewModel.userCreateState.observe(viewLifecycleOwner) { state ->
            if (state == UIState.Success) findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    fun navigateToLogin() {
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    fun signUp(email: String, password: String, repeatedPassword: String) {
        userViewModel.signUp(
            email,
            password,
            repeatedPassword
//            ,
//            requireContext().getString(R.string.check_passwords),
//            requireContext().getString(R.string.user_created),
//            requireContext().getString(R.string.authError)
        )
    }
}