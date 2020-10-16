package com.example.kotlinweatherapp.database

import androidx.room.*

@Dao
interface CurrentWeatherDao{
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
interface SelectedSingleForecastDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSelectedSingleForecastDatabaseClass(selectedSingleForecastDatabaseClass: SelectedSingleForecastDatabaseClass)

    @Query("SELECT * FROM selectedSingleForecastDatabaseClass")
    fun getSelectedSingleForecastDatabaseClass(): SelectedSingleForecastDatabaseClass
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
}