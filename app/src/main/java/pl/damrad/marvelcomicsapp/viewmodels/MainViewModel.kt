package pl.damrad.marvelcomicsapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okio.Timeout
import pl.damrad.marvelcomicsapp.states.UIState
import pl.damrad.marvelcomicsapp.repository.ComicsRepository
import pl.damrad.marvelcomicsapp.retrofit.NoConnectivityException
import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse
import pl.damrad.marvelcomicsapp.states.NetworkState
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainViewModel(
    private val repository: ComicsRepository
) : ViewModel() {

    private var _listOfComics = MutableLiveData<MarvelResponse>()
    val listOfComics: LiveData<MarvelResponse> get() = _listOfComics

    private var _listOfComicsByTitle = MutableLiveData<MarvelResponse>()
    val listOfComicsByTitle: LiveData<MarvelResponse> get() = _listOfComicsByTitle

    val infoSearchTextState = MutableLiveData(true)

    val progressBarState = MutableLiveData(false)

    val connectionState: MutableLiveData<NetworkState?> = MutableLiveData()

    private val handler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    private fun getComicsFromMarvelServer(offset: Int) = viewModelScope.launch(handler) {
        _listOfComics.value = repository.getComicsList(offset)
    }

    private fun getComicsByTitleFromMarvelServer(offset: Int, title: String) = viewModelScope.launch(handler) {
        _listOfComicsByTitle.value = repository.getComicsByTitle(offset, title)
        connectionState.value = NetworkState.Connected
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

    fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
    }

    private fun handleException(throwable: Throwable) {
        when (throwable) {
            is NoConnectivityException -> {
                connectionState.value = NetworkState.Disconnected
            }
            is HttpException -> {
                connectionState.value = NetworkState.Warning
            }
            is SocketTimeoutException -> {
                connectionState.value = NetworkState.Timeout
            }
        }
    }
}