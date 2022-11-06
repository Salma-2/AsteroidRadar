package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid


@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroid: List<AsteroidTable>)


    @Query("select * from asteroidtable order by close_approach_date")
    fun getAsteroids(): LiveData<List<AsteroidTable>>

//    @Query("select * from asteroidtable where close_approach_date = :date")
//    fun getTodayAsteroids(date: String): LiveData<List<AsteroidTable>>
}


@Database(entities = [AsteroidTable::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    if (!::INSTANCE.isInitialized) {
        synchronized(AsteroidDatabase::class.java) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid_database").build()
        }
    }
    return INSTANCE
}

