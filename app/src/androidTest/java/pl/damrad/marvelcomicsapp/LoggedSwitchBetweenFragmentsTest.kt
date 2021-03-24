package pl.damrad.marvelcomicsapp

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.damrad.marvelcomicsapp.activity.MainActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoggedSwitchBetweenFragmentsTest {

    private lateinit var navController: NavController

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initialize() {
        activityRule.scenario.onActivity { activity ->
            activity.findViewById<TextInputEditText>(R.id.emailET)?.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
            navController = activity.findNavController(R.id.fragment_nav_host)
        }
    }

    @After
    fun after(){
        Thread.sleep(1000)
        onView(withId(R.id.logoutBtn)).perform(click())
    }

    @Test
    fun switchFragments() {
        onView(withId(R.id.emailET)).perform(typeText("damrad@damrad.pl"), closeSoftKeyboard())
        onView(withId(R.id.passwordET)).perform(typeText("Damrad12"), closeSoftKeyboard())
        onView(withId(R.id.loginBtn)).perform(click())
        Thread.sleep(1000)
        assertThat(navController.currentDestination?.id, equalTo(R.id.comicsFragment))
    }
}