package pl.damrad.marvelcomicsapp.repository

import android.util.Patterns
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import pl.damrad.marvelcomicsapp.other.ValidationPatterns

class UserRepository {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !target.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isValidPassword(target: CharSequence?): Boolean {
        val regex = Regex(ValidationPatterns.EMAIL_PATTERN)
        return !target.isNullOrEmpty() && regex.matches(target)
    }

    fun comparePasswords(target: CharSequence?, target2: CharSequence?): Boolean {
        return !target.isNullOrEmpty() && !target2.isNullOrEmpty() && target == target2
    }

    fun signOut() {
        auth.signOut()
    }

    fun restorePassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }
}