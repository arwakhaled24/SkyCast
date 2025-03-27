package com.example.skycast.data.localData.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.skycast.data.LocalData.room.Dao
import com.example.skycast.data.LocalData.room.MyDatabase
import com.example.skycast.data.dataClasses.LocationDataClass
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class DaoTest {

    private lateinit var database: MyDatabase
    private lateinit var dao: Dao
    private val location = LocationDataClass(longitude = "11", latitude = "11", CityName = "kom")


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDatabase::class.java
        ).allowMainThreadQueries().build()


        dao = database.getDao()
    }


    @After
    fun colse() {
        database.close()
    }


    @Test
    fun getAllFavLocation() = runTest {
        val flow = dao.getAll().first()
        assertTrue(flow.isEmpty())
    }

    @Test
    fun insertFavLocation() = runTest {
        var rowID = dao.insertFavLocation(location)
        dao.deleteFavLocation(location)

        val flow = dao.getAll().first()
        assertThat(flow.size, `is`(1))
    }

    @Test
    fun deleteFavLocation() = runTest {

        dao.insertFavLocation(location)

        val inserted = dao.getAll().first() // list
        assertTrue(inserted.isNotEmpty())

        dao.deleteFavLocation(inserted.first()) //first in the list


        val flow = dao.getAll().first()
        assertTrue(flow.isEmpty())

    }


}