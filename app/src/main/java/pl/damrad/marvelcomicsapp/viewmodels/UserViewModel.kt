package pl.damrad.marvelcomicsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.damrad.marvelcomicsapp.other.UIState
import pl.damrad.marvelcomicsapp.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val isEmailValid: MutableLiveData<Boolean> = MutableLiveData()

    val isPasswordValid: MutableLiveData<Boolean> = MutableLiveData()

    val isRepeatedPasswordValid: MutableLiveData<Boolean> = MutableLiveData()

    val restorePassState: MutableLiveData<UIState?> = MutableLiveData()

    val authState: MutableLiveData<UIState?> = MutableLiveData()

    val userCreateState: MutableLiveData<UIState?> = MutableLiveData()

    fun signIn(
        email: String,
        password: String,
    ) = viewModelScope.launch {
        val checkEmail = userRepository.isValidEmail(email)
        val checkPass = userRepository.isValidPassword(password)

        isEmailValid.value = true
        isPasswordValid.value = true

        when {
            !checkEmail -> {
                isEmailValid.value = false
                return@launch
            }
            !checkPass -> {
                isPasswordValid.value = false
                return@launch
            }
        }

        userRepository.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let {
                        authState.value = UIState.Success
                    }
                }
                else -> {
                    authState.value = UIState.Error
                }
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        password2: String,
    ) = viewModelScope.launch {
        val checkEmail = userRepository.isValidEmail(email)
        val checkPass = userRepository.isValidPassword(password)
        val checkPass2 = userRepository.isValidPassword(password2)

        when {
            !checkEmail || !checkPass || !checkPass2 -> {
                return@launch
            }
            !comparePasswords(password, password2) -> {
                userCreateState.value = UIState.Warning
                return@launch
            }
        }

        userRepository.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    task.result?.user?.let {
                        userCreateState.value = UIState.Success
                    }
                }
                else -> {
                    userCreateState.value = UIState.Warning//task.exception?.message ?: authErrorText
                }
            }
        }
    }

    fun restorePassword(email: String) {
        if (userRepository.isValidEmail(email)) {
            userRepository.restorePassword(email)
            restorePassState.value = UIState.Success
        } else {
            restorePassState.value = UIState.Error
        }
    }

    fun checkEmailValid(target: CharSequence?) {
        isEmailValid.value = userRepository.isValidEmail(target)
    }

    fun checkPasswordValid(target: CharSequence?) {
        isPasswordValid.value = userRepository.isValidPassword(target)
    }

    fun checkRepeatedPasswordValid(target: CharSequence?) {
        isRepeatedPasswordValid.value = userRepository.isValidPassword(target)
    }

    private fun comparePasswords(target: CharSequence?, target2: CharSequence?): Boolean {
        return userRepository.comparePasswords(target, target2)
    }
}