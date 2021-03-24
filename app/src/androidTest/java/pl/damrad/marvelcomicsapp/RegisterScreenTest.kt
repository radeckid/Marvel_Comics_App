package pl.damrad.marvelcomicsapp

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.textfield.TextInputEditText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.damrad.marvelcomicsapp.activity.MainActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterScreenTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initialize() {
        onView(withId(R.id.createNewAccountBtn)).perform(click())
        activityRule.scenario.onActivity { activity ->
            activity.findViewById<TextInputEditText>(R.id.emailET)?.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }
    }

    @Test
    fun registerNewUser() {
        //register new user
        onView(withId(R.id.emailET)).perform(typeText("damrad@damrad.pl"), closeSoftKeyboard())
        onView(withId(R.id.passwordET)).perform(typeText("Damrad12"), closeSoftKeyboard())
        onView(withId(R.id.repeatPasswordET)).perform(typeText("Damrad12"), closeSoftKeyboard())
        onView(withId(R.id.registerBtn)).perform(click())
    }
}