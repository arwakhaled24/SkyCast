package com.example.skycast.data.localData

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.skycast.data.LocalData.LocalDataSource
import com.example.skycast.data.LocalData.room.Dao
import com.example.skycast.data.LocalData.room.MyDatabase
import com.example.skycast.data.dataClasses.LocationDataClass
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

class LocalDataSourceTest {
    private lateinit var database: MyDatabase
    private lateinit var dao: Dao
    private lateinit var locatDataSource:LocalDataSource
    private val location = LocationDataClass(longitude = "11", latitude = "11", CityName = "kom")

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.getDao()
        locatDataSource= LocalDataSource(dao)
    }


    @After
    fun colse() {
        database.close()
    }


    @Test
    fun getAllFavLocation() = runTest {
        val flow = locatDataSource.getAllFavLocation().first()
        assertTrue(flow.isEmpty())
    }

    @Test
    fun insertFavLocation() = runTest {
        var rowID = locatDataSource.addFavLocation(location)
        dao.deleteFavLocation(location)

        val flow = locatDataSource.getAllFavLocation().first()
        assertThat(flow.size, `is`(1))
    }

    @Test
    fun deleteFavLocation() = runTest {

        locatDataSource.addFavLocation(location)

        val inserted = locatDataSource.getAllFavLocation().first() // list
        assertTrue(inserted.isNotEmpty())

        locatDataSource.deleteFavLocation(inserted.first()) //first in the list


        val flow = locatDataSource.getAllFavLocation().first()
        assertTrue(flow.isEmpty())

    }


}