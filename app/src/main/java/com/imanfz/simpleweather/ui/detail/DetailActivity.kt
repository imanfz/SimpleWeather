package com.imanfz.simpleweather.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.imanfz.simpleweather.R
import com.imanfz.simpleweather.data.remote.response.DailyItem
import com.imanfz.simpleweather.databinding.ActivityDetailBinding
import com.imanfz.simpleweather.ui.base.BaseActivity
import com.imanfz.simpleweather.utils.getDayWithMonth
import com.imanfz.simpleweather.utils.getMonthYear
import com.imanfz.simpleweather.utils.loadImageFromUrl
import com.imanfz.simpleweather.utils.toCelsius
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class DetailActivity : BaseActivity<ActivityDetailBinding>(
    ActivityDetailBinding::inflate
) {

    private var dailyItem: DailyItem? = null
    private var day: String? = null
    private var location: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setupView()
        setupListener()
    }

    private fun init() {
        setupToolbar("Detail", isBackEnabled = true, isShareEnabled = true)
        dailyItem = intent.getParcelableExtra("data")
        day = intent.getStringExtra("day")
        location = intent.getStringExtra("location")
    }

    private fun setupListener() {
        binding.root.find<AppCompatImageView>(R.id.btn_share).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT, "$day Weather\n" +
                            "${dailyItem?.dt?.getDayWithMonth()} - ${dailyItem?.weather?.get(0)?.main}\n" +
                            "$location\n" +
                            "Temp : ${dailyItem?.temp?.day?.toCelsius()}")
                }
                startActivity(Intent.createChooser(intent, "Share"))
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        with(binding) {
            tvDay.text = day
            tvMonthYear.text = dailyItem?.dt?.getMonthYear()
            tvTemp.text = dailyItem?.temp?.day?.toCelsius()
            tvTempMin.text = dailyItem?.temp?.min?.toCelsius()
            dailyItem?.weather?.get(0)?.icon?.let { ivIcon.loadImageFromUrl(it) }
            tvWeatherStatus.text = dailyItem?.weather?.get(0)?.main
            tvHumidity.text = "Humadity: ${dailyItem?.humidity}%"
            tvPressure.text = "Pressure: ${dailyItem?.pressure} hPa"
            tvWind.text = "Wind: ${dailyItem?.windSpeed} km/h NE"
            tvLocatioin.text = "Location: $location"
        }
    }
}