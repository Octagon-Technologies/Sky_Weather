package com.octagon_technologies.sky_weather.repository.database

import androidx.room.*

// TODO - Consider base class, with commonly reused functions - Like Insert for example
// All other daos extend it, and since it takes a generic as an arguement, you can pass any data class to it - refer the MaishaMeds Interview for reference

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentForecastDataClass(currentForecastDatabaseClass: CurrentForecastDatabaseClass)

    @Query("SELECT * FROM currentForecastDatabaseClass")
    fun getCurrentForecastDataClass(): CurrentForecastDatabaseClass

}

@Dao
interface HourlyWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHourlyForecastDatabaseClass(arrayOfEachHourlyForecast: HourlyForecastDatabaseClass)

    @Query("SELECT * FROM hourlyForecastDatabaseClass")
    fun getHourlyForecastDatabaseClass(): HourlyForecastDatabaseClass
}

@Dao
interface DailyWeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDailyForecastDatabaseClass(arrayOfEachHourlyForecast: DailyForecastDatabaseClass)

    @Query("SELECT * FROM dailyForecastDatabaseClass")
    fun getDailyForecastDatabaseClass(): DailyForecastDatabaseClass
}

@Dao
interface AllergyForecastDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllergyForecastDatabaseClass(allergyDatabaseClass: AllergyDatabaseClass)

    @Query("SELECT * FROM allergyDatabaseClass")
    fun getAllergyForecastDatabaseClass(): AllergyDatabaseClass
}

@Dao
interface LunarForecastDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLunarForecastDatabaseClass(lunarDatabaseClass: LunarDatabaseClass)

    @Query("SELECT * FROM lunarDatabaseClass")
    fun getLunarForecastDatabaseClass(): LunarDatabaseClass
}

@Dao
interface LocationDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationDatabaseClass(locationDatabaseClass: LocationDatabaseClass)

    @Query("SELECT * FROM locationDatabaseClass")
    fun getLocationDatabaseClass(): LocationDatabaseClass
}

@Dao
interface FavouriteLocationDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteLocationDatabaseClass(favouriteLocationDatabaseClass: FavouriteLocationDatabaseClass)

    @Delete
    fun deleteFavouriteLocationDatabaseClass(favouriteLocationDatabaseClass: FavouriteLocationDatabaseClass)

    @Query("SELECT * FROM favouriteLocationDatabaseClass")
    fun getFavouriteLocationsDatabaseClass(): List<FavouriteLocationDatabaseClass>?

    @Delete
    fun deleteAllFavourite(listOfFavouriteLocations: List<FavouriteLocationDatabaseClass>)
}

@Dao
interface RecentLocationDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecentLocationDatabaseClass(recentLocationDatabaseClass: RecentLocationDatabaseClass)

    @Delete
    fun deleteRecentLocationDatabaseClass(recentLocationDatabaseClass: RecentLocationDatabaseClass)

    @Query("SELECT * FROM recentLocationDatabaseClass")
    fun getRecentLocationsDatabaseClass(): List<RecentLocationDatabaseClass>?

    @Delete
    fun deleteAllRecent(listOfRecentLocations: List<RecentLocationDatabaseClass>)
}