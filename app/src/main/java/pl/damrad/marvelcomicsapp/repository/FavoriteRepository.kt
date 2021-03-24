package pl.damrad.marvelcomicsapp.repository

import kotlinx.coroutines.flow.Flow
import pl.damrad.marvelcomicsapp.room.ComicsDao
import pl.damrad.marvelcomicsapp.room.model.Comics

class FavoriteRepository(
    private val comicsDao: ComicsDao
) {

    fun allComics(email: String): Flow<List<Comics>> = comicsDao.getAllFavorites(email)

    fun insertComics(comics: Comics, email: String) {
        comics.apply {
            loggedUser = email
        }
        comicsDao.insertComics(comics)
    }

    suspend fun deleteComics(morePath: String, email: String) = comicsDao.deleteComics(morePath, email)

    fun getComicByDetailPath(morePath: String, email: String) = comicsDao.getComicByDetailPath(morePath, email)
}