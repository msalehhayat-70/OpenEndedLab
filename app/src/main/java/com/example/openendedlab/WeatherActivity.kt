package com.example.openendedlab

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherActivity : AppCompatActivity() {

    private lateinit var weatherTextView: TextView
    private val apiKey = "a984bb574d07a1c17e3d3d3474f79322"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherTextView = findViewById(R.id.weatherTextView)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApiService = retrofit.create(WeatherApiService::class.java)

        val call = weatherApiService.getCurrentWeather("Lahore", apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val weatherInfo = "City: ${weatherResponse?.name}\n" +
                            "Temperature: ${weatherResponse?.main?.temp}°C\n" +
                            "Description: ${weatherResponse?.weather?.get(0)?.description}"
                    weatherTextView.text = weatherInfo
                } else {
                    weatherTextView.text = "Failed to get weather data"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherTextView.text = "Error: ${t.message}"
            }
        })
    }
}

interface WeatherApiService {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>
}

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String
)
