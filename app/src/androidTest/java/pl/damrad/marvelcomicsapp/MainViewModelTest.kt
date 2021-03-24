package pl.damrad.marvelcomicsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import pl.damrad.marvelcomicsapp.TestUtils.blockingObserve
import pl.damrad.marvelcomicsapp.repository.ComicsRepository
import pl.damrad.marvelcomicsapp.retrofit.MarvelApi
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel


@RunWith(JUnit4::class)
class MainViewModelTest {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var marvelApi: MarvelApi
    private lateinit var repository: ComicsRepository

    @Before
    fun initialize() {
        MockitoAnnotations.initMocks(this)
        repository = ComicsRepository(marvelApi)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun getComicsFromServer_Offset25() {
        viewModel.getAllComics()
        val result = viewModel.listOfComics.blockingObserve()
        assertThat(result, equalTo(null))
    }

}