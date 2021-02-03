package pl.damrad.marvelcomicsapp.retrofit

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpClientBuilder {

    private val client = OkHttpClient.Builder()

    private fun retrofit(context: Context): Retrofit = Retrofit.Builder()
        .baseUrl(Paths.BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            client
                .addInterceptor(NetworkConnectionInterceptor(context))
                .build()
        )
        .build()

    fun <T> buildService(context: Context, service: Class<T>): T {
        return retrofit(context).create(service)
    }

}