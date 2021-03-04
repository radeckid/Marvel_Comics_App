package pl.damrad.marvelcomicsapp.repository

import kotlinx.coroutines.flow.Flow
import pl.damrad.marvelcomicsapp.room.ComicsDao
import pl.damrad.marvelcomicsapp.room.model.Comics

class FavoriteRepository(
    private val comicsDao: ComicsDao
) {

    val allComics: Flow<List<Comics>> = comicsDao.getAllFavorites()

    suspend fun insertComics(comics: Comics) = comicsDao.insertComics(comics)

    suspend fun deleteComics(morePath: String) = comicsDao.deleteComics(morePath)

    fun getComicByDetailPath(morePath: String) = comicsDao.getComicByDetailPath(morePath)
}