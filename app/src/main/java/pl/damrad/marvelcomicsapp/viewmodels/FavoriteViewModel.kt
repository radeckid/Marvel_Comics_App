package pl.damrad.marvelcomicsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.repository.FavoriteRepository
import pl.damrad.marvelcomicsapp.room.model.Comics

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    val favoriteComicState = MutableLiveData<Boolean>()

    fun getAllComics() = favoriteRepository.allComics.map { comics ->
        val list = arrayListOf<ComicsItem?>()
        for (item in comics) {
            list.add(
                ComicsItem(
                    title = item.title,
                    author = item.author,
                    description = item.description,
                    imagePath = item.imagePath,
                    morePath = item.morePath
                )
            )
        }
        list
    }.asLiveData()

    fun insertComics(comics: Comics) = viewModelScope.launch {
        favoriteRepository.insertComics(comics)
    }

    fun deleteComics(morePath: String) = viewModelScope.launch {
        favoriteRepository.deleteComics(morePath)
    }

    fun getComicByDetailPath(morePath: String) = favoriteRepository.getComicByDetailPath(morePath)
}