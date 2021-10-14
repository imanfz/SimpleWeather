package com.imanfz.simpleweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imanfz.simpleweather.data.remote.response.DailyWeatherResponse
import com.imanfz.simpleweather.data.repository.DailyWeatherRepository
import com.imanfz.simpleweather.utils.UiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DailyWeatherViewModel(
    private val dailyWeatherRepository: DailyWeatherRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val dailyWeatherState by lazy {
        MutableLiveData<UiState<DailyWeatherResponse>>()
    }

    fun getDailyWeather(
        lat: Double,
        lon: Double
    ) {
        dailyWeatherState.value = UiState.Loading()
        compositeDisposable.add(
            dailyWeatherRepository.getDailyWeather(lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    dailyWeatherState.value = UiState.Success(it)
                }, {
                    dailyWeatherState.value = UiState.Error(it)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}