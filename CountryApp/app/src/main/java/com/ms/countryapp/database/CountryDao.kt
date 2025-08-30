package com.ms.countryapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ms.countryapp.data.Country

@Dao
interface CountryDao: ICountryDao {

    @Query("SELECT * from Country")
    override suspend fun getAllCountries(): List<Country>

    @Query("SELECT * from Country WHERE continents LIKE :continent")
    override suspend fun getCountriesByContinent(continent: String): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertAll(countries: List<Country>)

    @Delete()
    override suspend fun delete(country: Country)

    @Query("UPDATE Country set capital = :capital where id = :id")
    override suspend fun updateCapital(capital: List<String>, id: Int): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun updateCountry(country: Country): Int
}