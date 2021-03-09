package pl.damrad.marvelcomicsapp.repository

import android.util.Log
import pl.damrad.marvelcomicsapp.other.Key
import pl.damrad.marvelcomicsapp.retrofit.MarvelApi
import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse

class ComicsRepository(
    private val marvelApi: MarvelApi
) {

    suspend fun getComicsList(offset: Int): MarvelResponse {

        val parameters = mapOf(
            "ts" to "1",
            "apikey" to Key.PUBLIC_KEY,
            "hash" to Key.HASH,
            "limit" to "25",
            "offset" to offset.toString(),
            "orderBy" to "-onsaleDate"
        )

        val tmp = marvelApi.getComics(parameters).also {
            Log.e("MYRESPONSE", it.status.toString())
        }
        return tmp
    }

    suspend fun getComicsByTitle(offset: Int, title: String): MarvelResponse {

        val parameters = mapOf(
            "ts" to "1",
            "apikey" to Key.PUBLIC_KEY,
            "hash" to Key.HASH,
            "limit" to "25",
            "offset" to offset.toString(),
            "title" to title
        )

        return marvelApi.getComics(parameters)
    }
}