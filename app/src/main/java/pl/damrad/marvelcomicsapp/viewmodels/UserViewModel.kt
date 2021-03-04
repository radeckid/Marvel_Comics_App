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

    private val _isRepeatedPasswordValid: MutableLiveData<Boolean> = MutableLiveData()
    val isRepeatedPasswordValid: LiveData<Boolean> get() = _isRepeatedPasswordValid

    private val _toast: MutableLiveData<String?> = MutableLiveData()
    val toast: LiveData<String?> get() = _toast

    private val _authStatus: MutableLiveData<Boolean> = MutableLiveData()
    val authStatus: LiveData<Boolean> get() = _authStatus

    private val _userCreateStatus: MutableLiveData<Boolean> = MutableLiveData()
    val userCreateStatus: LiveData<Boolean> get() = _userCreateStatus

    fun signIn(email: String, password: String, context: Context) = viewModelScope.launch {
        val checkEmail = userRepository.isValidEmail(email)
        val checkPass = userRepository.isValidPassword(password)

        if (!checkEmail && !checkPass) return@launch

        userRepository.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let { _ ->
                        _toast.value = context.getString(R.string.logged_in)
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

    fun signUp(email: String, password: String, password2: String, context: Context) = viewModelScope.launch {
        val checkEmail = userRepository.isValidEmail(email)
        val checkPass = userRepository.isValidPassword(password)
        val checkPass2 = userRepository.isValidPassword(password2)

        when {
            !checkEmail || !checkPass || !checkPass2 -> {
                return@launch
            }
            !comparePasswords(password, password2) -> {
                _toast.value = context.getString(R.string.check_passwords)
                onToastShown()
                return@launch
            }
        }

        userRepository.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let { user ->
                        _toast.value = context.getString(R.string.user_created)
                        _userCreateStatus.value = true
                    }
                }
                else -> {
                    _toast.value = task.exception?.message ?: context.getString(R.string.authError)
                    _userCreateStatus.value = false
                }
            }
        }
        onToastShown()
    }

    fun signOut() {
        userRepository.signOut()
        _authStatus.value = false
    }

    fun restorePassword(email: String, context: Context) {
        if (userRepository.isValidEmail(email)) {
            userRepository.restorePassword(email)
            _toast.value = context.getString(R.string.check_your_email)
            onToastShown()
        } else {
            _toast.value = context.getString(R.string.enter_the_address_in_the_field)
            onToastShown()
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

    fun checkRepeatedPasswordValid(target: CharSequence?) {
        _isRepeatedPasswordValid.value = userRepository.isValidPassword(target)
    }

    private fun comparePasswords(target: CharSequence?, target2: CharSequence?): Boolean {
        return userRepository.comparePasswords(target, target2)
    }
}