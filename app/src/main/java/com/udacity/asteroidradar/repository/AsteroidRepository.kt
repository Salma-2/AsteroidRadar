package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.MainViewModel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database: AsteroidDatabase) {

    // get data from DB
    val asteroidList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun getPictureOfDay(): PictureOfDay? {
        return AsteroidApi.retrofitService.getPictureOfDay(Constants.API_KEY)
    }


    //Get Data from API and Insert It Into DB
    suspend fun refreshAsteroids() {
        Log.i("Repo", "refresh data")
        withContext(Dispatchers.IO) {
            // get asteroids from network
            val asteroidListStr =
                AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)

            // convert it
            val asteroidListJson = JSONObject(asteroidListStr)
            val asteroidList: ArrayList<Asteroid> = parseAsteroidsJsonResult(asteroidListJson)

            // insert data into db
            database.asteroidDao.insertAll(asteroidList.asDatabaseModel())
        }

    }
}