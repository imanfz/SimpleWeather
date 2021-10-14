package com.imanfz.simpleweather.data.repository

import com.imanfz.simpleweather.data.remote.response.DailyWeatherResponse
import com.imanfz.simpleweather.data.remote.service.ApiService
import io.reactivex.Single

class DailyWeatherRepository(
    private val apiService: ApiService
) {

    fun getDailyWeather(
        lat: Double,
        lon: Double
    ) : Single<DailyWeatherResponse> {
        return apiService.getDailyWeather(lat, lon)
    }
}