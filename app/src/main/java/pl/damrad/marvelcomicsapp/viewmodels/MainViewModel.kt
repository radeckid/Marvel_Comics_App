package pl.damrad.marvelcomicsapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import pl.damrad.marvelcomicsapp.repository.ComicsRepository
import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse

class MainViewModel(
    private val repository: ComicsRepository
) : ViewModel() {

    private var _listOfComics = MutableLiveData<MarvelResponse>()
    val listOfComics: LiveData<MarvelResponse> get() = _listOfComics

    private var _listOfComicsByTitle = MutableLiveData<MarvelResponse>()
    val listOfComicsByTitle: LiveData<MarvelResponse> get() = _listOfComicsByTitle

    private val _connectionState = MutableLiveData<Boolean>()
    val connectionState: LiveData<Boolean> = _connectionState

    private val handler = CoroutineExceptionHandler { _, throwable ->
        throwable.stackTraceToString()
        setState(false)
    }

    private fun getComicsFromMarvelServer(offset: Int) = viewModelScope.launch(handler) {
        _listOfComics.value = repository.getComicsList(offset)
        setState(true)
    }

    private fun getComicsByTitleFromMarvelServer(offset: Int, title: String) = viewModelScope.launch(handler) {
        _listOfComicsByTitle.value = repository.getComicsByTitle(offset, title)
        setState(true)
    }

    private var offset: Int = 0
    fun nextOffset() {
        offset += 25
    }

    private var offsetTitle: Int = 0
    fun nextOffsetTitle() {
        offsetTitle += 25
    }

    fun setOffsetTitle(offset: Int) {
        offsetTitle = offset
    }

    fun getAllComics() {
        getComicsFromMarvelServer(offset)
    }

    fun getAllComicsByTitle(title: String) {
        getComicsByTitleFromMarvelServer(offset, title)
    }

    fun setState(state: Boolean) {
        _connectionState.postValue(state)
    }
}