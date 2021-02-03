package pl.damrad.marvelcomicsapp.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException("No internet connection")
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.activeNetwork ?: return false
        val info = connectivityManager.getNetworkCapabilities(capabilities) ?: return false
        return when {
            info.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            info.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            info.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}