package pl.damrad.marvelcomicsapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.damrad.marvelcomicsapp.R
import pl.damrad.marvelcomicsapp.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isEmailValid: MutableLiveData<Boolean> = MutableLiveData()
    val isEmailValid: LiveData<Boolean> get() = _isEmailValid

    private val _isPasswordValid: MutableLiveData<Boolean> = MutableLiveData()
    val isPasswordValid: LiveData<Boolean> get() = _isPasswordValid

    private val _toast: MutableLiveData<String?> = MutableLiveData()
    val toast: LiveData<String?> get() = _toast

    private val _authStatus: MutableLiveData<Boolean> = MutableLiveData()
    val authStatus: LiveData<Boolean> get() = _authStatus

    fun signIn(email: String, password: String, context: Context) = viewModelScope.launch {
        userRepository.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let { user ->
                        _toast.value = user.email
                        _authStatus.value = true
                    }
                }
                else -> {
                    _toast.value = context.getString(R.string.authError)
                    _authStatus.value = false
                }
            }
        }
    }

    fun onToastShown() {
        _toast.value = null
    }

    fun checkEmailValid(target: CharSequence?) {
        _isEmailValid.value = userRepository.isValidEmail(target)
    }

    fun checkPasswordValid(target: CharSequence?) {
        _isPasswordValid.value = userRepository.isValidPassword(target)
    }
}