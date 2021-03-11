package pl.damrad.marvelcomicsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.databinding.FragmentRegistrationBinding
import pl.damrad.marvelcomicsapp.states.UIState
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

        userViewModel.isRepeatedPasswordValid.observe(viewLifecycleOwner) { result ->
            if (!result) {
                binding?.repeatPasswordTextField?.error = getString(R.string.passwordError)
            } else {
                binding?.repeatPasswordTextField?.error = null
            }
        }

        userViewModel.userCreateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    Toasty.success(requireContext(), R.string.user_created, Toast.LENGTH_LONG, true).show()
                }
                is UIState.Warning -> {
                    Toasty.warning(requireContext(), R.string.check_passwords, Toast.LENGTH_LONG, true).show()
                }
                is UIState.Error -> {
                    Toasty.error(requireContext(), R.string.authError, Toast.LENGTH_LONG, true).show()
                }
                is UIState.ErrorResponse -> {
                    state.text?.let { Toasty.error(requireContext(), it, Toast.LENGTH_LONG, true).show() }
                }
            }
        }
    }

    fun navigateToLogin() {
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }
}