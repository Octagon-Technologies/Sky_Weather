package com.octagontechnologies.sky_weather.repository

import com.octagontechnologies.sky_weather.domain.Location
import com.octagontechnologies.sky_weather.notification.CustomNotificationCompat
import com.octagontechnologies.sky_weather.repository.repo.CurrentForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.DailyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.HourlyForecastRepo
import com.octagontechnologies.sky_weather.repository.repo.LocationRepo
import com.octagontechnologies.sky_weather.repository.repo.LunarRepo
import com.octagontechnologies.sky_weather.repository.repo.SettingsRepo
import com.octagontechnologies.sky_weather.utils.Units
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WeatherRepoTest {
    private lateinit var locationRepo: LocationRepo
    private lateinit var settingsRepo: SettingsRepo
    private lateinit var currentForecastRepo: CurrentForecastRepo
    private lateinit var hourlyForecastRepo: HourlyForecastRepo
    private lateinit var dailyForecastRepo: DailyForecastRepo
    private lateinit var lunarRepo: LunarRepo
    private lateinit var customNotificationCompat: CustomNotificationCompat

    // 2. Declare the class under test
    private lateinit var weatherRepo: WeatherRepo

    // 3. Setup function to initialize mocks before each test
    @BeforeEach
    fun setUp() {
        locationRepo = mockk(relaxed = true)
        settingsRepo = mockk(relaxed = true)
        currentForecastRepo = mockk(relaxed = true)
        hourlyForecastRepo = mockk(relaxed = true)
        dailyForecastRepo = mockk(relaxed = true)
        lunarRepo = mockk(relaxed = true)
        customNotificationCompat = mockk(relaxed = true)

        weatherRepo =
            WeatherRepo(
                locationRepo,
                settingsRepo,
                currentForecastRepo,
                hourlyForecastRepo,
                dailyForecastRepo,
                lunarRepo,
                customNotificationCompat,
            )
    }

    @Test
    @DisplayName("Given a valid location, when refreshUrgentForecast is called, then it should refresh data and notification")
    fun `refreshUrgentForecast should refresh data and notification`() =
        runTest {
            val fakeLocation =
                Location(
                    lat = "40.3",
                    lon = "40.3",
                    displayNameWithoutCountryCode = "Karen",
                    country = "Kenya",
                    countryCode = "KE",
                    isGps = false,
                )

            val fakeUnits = Units.METRIC
            val fakeNotificationAllowed = true

            every { settingsRepo.units.value } returns fakeUnits
            every { settingsRepo.isNotificationAllowed.value } returns fakeNotificationAllowed
            every { locationRepo.location } returns flowOf(fakeLocation)

            weatherRepo.refreshCurrentAndHourlyForecast()

            coVerify(exactly = 1) {
                currentForecastRepo.refreshCurrentForecast(fakeLocation)
                hourlyForecastRepo.refreshHourlyForecast(fakeLocation)

                customNotificationCompat.createNotification(
                    singleForecast = any(),
                    location = fakeLocation,
                    units = fakeUnits,
                )
            }
        }

    @Test
    @DisplayName("Given notifications are off, when refreshUrgentForecast is called, then it should only refresh data")
    fun `refreshUrgentForecast should refresh data but not notification`() =
        runTest {
            val fakeLocation =
                Location(
                    lat = "40.3",
                    lon = "40.3",
                    displayNameWithoutCountryCode = "Karen",
                    country = "Kenya",
                    countryCode = "KE",
                    isGps = false,
                )

            val fakeUnits = Units.METRIC
            val fakeNotificationAllowed = false

            every { settingsRepo.units.value } returns fakeUnits
            every { settingsRepo.isNotificationAllowed.value } returns fakeNotificationAllowed
            every { locationRepo.location } returns flowOf(fakeLocation)

            weatherRepo.refreshCurrentAndHourlyForecast()

            coVerify(exactly = 1) {
                currentForecastRepo.refreshCurrentForecast(fakeLocation)
                hourlyForecastRepo.refreshHourlyForecast(fakeLocation)
            }

            coVerify(exactly = 0) {
                customNotificationCompat.createNotification(
                    singleForecast = any(),
                    location = fakeLocation,
                    units = fakeUnits,
                )
            }
        }
}
