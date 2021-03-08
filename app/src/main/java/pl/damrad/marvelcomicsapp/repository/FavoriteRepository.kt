package pl.damrad.marvelcomicsapp.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import pl.damrad.marvelcomicsapp.room.ComicsDao
import pl.damrad.marvelcomicsapp.room.model.Comics

class FavoriteRepository(
    private val comicsDao: ComicsDao
) {

    private val auth = FirebaseAuth.getInstance()

    val allComics: Flow<List<Comics>> = auth.currentUser?.email?.let { comicsDao.getAllFavorites(it) } ?: run { flowOf(emptyList()) }

    suspend fun insertComics(comics: Comics) {
        auth.currentUser?.email?.let { user ->
            comics.apply {
                loggedUser = user
            }
            comicsDao.insertComics(comics)
        }
    }

    suspend fun deleteComics(morePath: String) = auth.currentUser?.email?.let { comicsDao.deleteComics(morePath, it) }

    fun getComicByDetailPath(morePath: String) = auth.currentUser?.email?.let { comicsDao.getComicByDetailPath(morePath, it) }
}