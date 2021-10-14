package com.imanfz.simpleweather.data.remote.service

import com.imanfz.simpleweather.BuildConfig.API_KEY
import com.imanfz.simpleweather.data.remote.response.DailyWeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/onecall")
    fun getDailyWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "hourly,minutely",
        @Query("appid") appid: String = API_KEY
    ): Single<DailyWeatherResponse>

}