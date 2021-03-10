package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.FragmentLoginBinding
import pl.damrad.marvelcomicsapp.other.UIState
import pl.damrad.marvelcomicsapp.viewmodels.UserViewModel

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = FragmentLoginBinding.inflate(inflater, container, false).apply {
            fragment = this@LoginFragment
            userVM = userViewModel
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

        userViewModel.isPasswordValid.observe(viewLifecycleOwner) { result ->
            if (!result) {
                binding?.passwordTextField?.error = getString(R.string.passwordError)
            } else {
                binding?.passwordTextField?.error = null
            }
        }

        userViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_comicsFragment)
                    Toasty.success(requireContext(), R.string.logged_in, Toast.LENGTH_SHORT, true).show()
                }
                is UIState.Error -> {
                    Toasty.error(requireContext(), R.string.authError, Toast.LENGTH_LONG, true).show()
                }
            }
        }

        userViewModel.restorePassState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {
                    Toasty.success(requireContext(), R.string.check_your_email, Toast.LENGTH_LONG, true).show()
                }
                is UIState.Error -> {
                    Toasty.error(requireContext(), R.string.enter_the_address_in_the_field, Toast.LENGTH_LONG, true).show()
                }
            }
        }
    }

    fun navigateToRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
    }
}