package com.imanfz.simpleweather.di

import com.imanfz.simpleweather.BuildConfig.BASE_URL
import com.imanfz.simpleweather.data.remote.service.ApiService
import com.imanfz.simpleweather.data.remote.service.AuthInterceptor
import com.imanfz.simpleweather.data.remote.service.createWebService
import com.imanfz.simpleweather.data.remote.service.provideOkHttpClient
import com.imanfz.simpleweather.data.repository.DailyWeatherRepository
import com.imanfz.simpleweather.viewmodel.DailyWeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AuthInterceptor() }
    single { provideOkHttpClient(get(), androidContext()) }
    single { createWebService<ApiService>(get(), BASE_URL) }
}

val repoModule = module {
    single { DailyWeatherRepository(get()) }
}

val viewModelModule = module {
    viewModel { DailyWeatherViewModel(get()) }
}

val myModule = listOf(
    appModule,
    repoModule,
    viewModelModule
)