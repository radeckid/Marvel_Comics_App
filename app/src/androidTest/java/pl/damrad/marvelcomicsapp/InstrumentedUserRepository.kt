package pl.damrad.marvelcomicsapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.damrad.marvelcomicsapp.repository.UserRepository

@RunWith(AndroidJUnit4::class)
class InstrumentedUserRepository {

    lateinit var repository: UserRepository

    @Before
    fun instantiate() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
        repository = UserRepository()
    }

    @Test
    fun emailValidation_Correct_True() {
        //given
        val email1 = "testMial@malil.com"
        //when
        val result = repository.isValidEmail(email1)
        //then
        assert(result)
    }

    @Test
    fun emailValidation_Incorrect_False() {
        //given
        val email1 = "testMial@malilcom"
        //when
        val result = repository.isValidEmail(email1)
        //then
        assert(!result)
    }

    @Test
    fun passwordValidation_Correct_True() {
        //given
        val password1 = "TestPassword123"
        //when
        val result = repository.isValidPassword(password1)
        //then
        assert(result)
    }

    @Test
    fun passwordValidation_Incorrect_False() {
        //given
        val password1 = "simple1"
        //when
        val result = repository.isValidPassword(password1)
        //then
        assert(!result)
    }

    @Test
    fun comparePasswords_Correct_True() {
        //given
        val password1 = "TestPassword123"
        val password2 = "TestPassword123"
        //when
        val result = repository.comparePasswords(password1, password2)
        //then
        assert(result)
    }

    @Test
    fun comparePasswords_Incorrect_False() {
        //given
        val password1 = "TestPassword2123"
        val password2 = "TestPassword1323"
        //when
        val result = repository.comparePasswords(password1, password2)
        //then
        assert(!result)
    }

}