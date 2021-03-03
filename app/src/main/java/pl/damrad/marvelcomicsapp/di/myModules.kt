package pl.damrad.marvelcomicsapp.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.damrad.marvelcomicsapp.repository.ComicsRepository
import pl.damrad.marvelcomicsapp.repository.UserRepository
import pl.damrad.marvelcomicsapp.retrofit.HttpClientBuilder
import pl.damrad.marvelcomicsapp.retrofit.MarvelApi
import pl.damrad.marvelcomicsapp.viewmodels.MainViewModel
import pl.damrad.marvelcomicsapp.viewmodels.UserViewModel

val myModules = module {

    single { provideRetrofit(androidContext()) }
    single { ComicsRepository(get()) }
    viewModel { MainViewModel(get()) }

    single { UserRepository() }
    viewModel { UserViewModel(get()) }
}

fun provideRetrofit(context: Context) = HttpClientBuilder.buildService(context, MarvelApi::class.java)
