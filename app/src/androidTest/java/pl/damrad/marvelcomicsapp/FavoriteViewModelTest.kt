package pl.damrad.marvelcomicsapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.damrad.marvelcomicsapp.TestUtils.blockingObserve
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.repository.FavoriteRepository
import pl.damrad.marvelcomicsapp.room.ComicsRoomDatabase
import pl.damrad.marvelcomicsapp.room.model.Comics
import pl.damrad.marvelcomicsapp.viewmodels.FavoriteViewModel
import java.io.IOException

@RunWith(JUnit4::class)
class FavoriteViewModelTest {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var db: ComicsRoomDatabase

    @Rule
    @JvmField val rule = InstantTaskExecutorRule()

    @Before
    fun initialize() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(
            context, ComicsRoomDatabase::class.java
        ).build()
        val comicsDao = db.comicsDao()
        val repository = FavoriteRepository(comicsDao)
        viewModel = FavoriteViewModel(repository)

    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_addNewComics_true() {
        //given
        val comics = Comics(
            title = "Test",
            description = "test",
            loggedUser = "test",
            morePath = "test",
            imagePath = "test",
            author = "test"
        )

        val email = "test"
        //when
        db.comicsDao().insertComics(comics)
        //then
        val item = ComicsItem(comics.title, comics.author, comics.description, comics.imagePath, comics.morePath)
        val result = db.comicsDao().getComicByDetailPath(comics.morePath!!, email).blockingObserve()
        Assert.assertEquals(result?.author, item.author)
    }
}