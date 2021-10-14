package com.imanfz.simpleweather.ui.main

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.imanfz.simpleweather.R
import com.imanfz.simpleweather.data.remote.response.DailyItem
import com.imanfz.simpleweather.data.remote.response.DailyWeatherResponse
import com.imanfz.simpleweather.databinding.ActivityMainBinding
import com.imanfz.simpleweather.ui.base.BaseActivity
import com.imanfz.simpleweather.ui.detail.DetailActivity
import com.imanfz.simpleweather.utils.*
import com.imanfz.simpleweather.viewmodel.DailyWeatherViewModel
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    private val vm: DailyWeatherViewModel by inject()
    private lateinit var mainAdapter: MainAdapter
    private var today: DailyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setupListener()
        setupObserver()
    }

    private fun init() {
        mainAdapter = MainAdapter(object : MainAdapter.OnClickListener {
            override fun onClick(data: DailyItem) {
                startActivity<DetailActivity>(
                    "data" to data,
                    "day" to data.dt?.getDay(),
                    "location" to binding.tvLocation.text
                )
            }
        })
        binding.rvWeather.apply {
            val linearLayoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager = linearLayoutManager
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    linearLayoutManager.orientation
                )
            )
            setHasFixedSize(true)
            adapter = mainAdapter
        }
    }

    private fun setupListener() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                vm.getDailyWeather(-7.385735, 109.360430)
            }

            btnToday.setOnClickListener {
                startActivity<DetailActivity>(
                    "data" to today,
                    "day" to "Today",
                    "location" to binding.tvLocation.text
                )
            }
        }
    }

    private fun setupObserver() {
        vm.getDailyWeather(-7.385735, 109.360430)
        vm.dailyWeatherState.observe(this, {
            when (it) {
                is UiState.Loading -> {
                    with(binding) {
                        showSkeletonList(
                            rvWeather,
                            R.layout.skeleton_list_weather,
                            rvWeather.adapter
                        )
                    }
                }
                is UiState.Success -> {
                    hideSkeletonList()
                    if (it.data.daily?.isNotEmpty() == true) {
                        setDataToUi(it.data)
                    } else {
                        Log.e("TAG", "setupObserver: " + it.data.message )
                    }
                }
                is UiState.Error -> {
                    hideSkeletonList()
                    Log.e("TAG", "setupObserver: " + it.throwable.message )
                }
            }
        })
    }

    private fun setDataToUi(data: DailyWeatherResponse) {
        with(binding) {
            tvLocation.text = data.timezone
            with(data.current) {
                tvDate.text = this?.dt?.getDayWithMonth()
                tvTemp.text = this?.temp?.toCelsius()
                tvTempMin.text = this?.dewPoint?.toCelsius()
                tvWeatherStatus.text = this?.weather?.get(0)?.main
                tvWeatherDesc.text = this?.weather?.get(0)?.description
                this?.weather?.get(0)?.icon!!.let {
                    ivIconWeatherToolbar.loadImageFromUrl(it)
                    ivWeatherIcon.loadImageFromUrl(it)
                }
            }
        }
        data.daily?.let {
            mainAdapter.setList(it.subList(1, it.size-1))
            today = it[0]
        }
    }
}