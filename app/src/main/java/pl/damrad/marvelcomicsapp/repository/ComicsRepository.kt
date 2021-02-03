package pl.damrad.marvelcomicsapp.repository

import pl.damrad.marvelcomicsapp.BuildConfig
import pl.damrad.marvelcomicsapp.retrofit.MarvelApi
import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse
import retrofit2.Response

class ComicsRepository(
    private val marvelApi: MarvelApi
) {

    suspend fun getComicsList(offset: Int): MarvelResponse {

        val parameters = mapOf(
            "ts" to "1",
            "apikey" to BuildConfig.API_KEY,
            "hash" to BuildConfig.API_HASH,
            "limit" to "25",
            "offset" to offset.toString(),
            "orderBy" to "-onsaleDate"
        )

        return marvelApi.getComics(parameters)
    }

    suspend fun getComicsByTitle(offset: Int, title: String): MarvelResponse {

        val parameters = mutableMapOf<String, String>(
            "ts" to "1",
            "apikey" to BuildConfig.API_KEY,
            "hash" to BuildConfig.API_HASH,
            "limit" to "25",
            "offset" to offset.toString(),
            "title" to title
        )

        return marvelApi.getComics(parameters)
    }
}