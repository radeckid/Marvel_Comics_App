package pl.damrad.marvelcomicsapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

    fun signIn(
        email: String,
        password: String,
        loggedInText: String,
        authErrorText: String
    ) = viewModelScope.launch {
        val checkEmail = userRepository.isValidEmail(email)
        val checkPass = userRepository.isValidPassword(password)

        if (!checkEmail && !checkPass) return@launch

        userRepository.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let {
                        _toast.value = loggedInText
                        _authStatus.value = true
                    }
                }
                else -> {
                    _toast.value = authErrorText
                    _authStatus.value = false
                }
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        password2: String,
        comparePassErrorText: String,
        createSuccessText: String,
        authErrorText: String
    ) = viewModelScope.launch {
        val checkEmail = userRepository.isValidEmail(email)
        val checkPass = userRepository.isValidPassword(password)
        val checkPass2 = userRepository.isValidPassword(password2)

        when {
            !checkEmail || !checkPass || !checkPass2 -> {
                return@launch
            }
            !comparePasswords(password, password2) -> {
                _toast.value = comparePassErrorText
                onToastShown()
                return@launch
            }
        }

        userRepository.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let {
                        _toast.value = createSuccessText
                        _userCreateStatus.value = true
                    }
                }
                else -> {
                    _toast.value = task.exception?.message ?: authErrorText
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

    fun restorePassword(email: String, successText: String, errorText: String) {
        if (userRepository.isValidEmail(email)) {
            userRepository.restorePassword(email)
            _toast.value = successText
            onToastShown()
        } else {
            _toast.value = errorText
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