package pl.damrad.marvelcomicsapp.retrofit

import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MarvelApi {

    @GET(Paths.COMICS)
    suspend fun getComics(@QueryMap params: Map<String, String>): MarvelResponse
}