package com.example.skycast.data.repository

import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.localData.LocaldataFake
import com.example.skycast.data.remoteData.RemoteDataFack
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test


class WeatherRepositoryTest {


    private lateinit var fakeRemoteDataSource:RemoteDataFack
    private lateinit var fakeLocaldataSource: LocaldataFake
    private lateinit var repository: WeatherRepository
    val addedLocation:LocationDataClass=LocationDataClass(id= 78, latitude = "78", longitude = "78", CityName = "arwa")


    @Before
    fun setUp(){
        fakeLocaldataSource= LocaldataFake()
        fakeRemoteDataSource= RemoteDataFack()
        repository=WeatherRepository.getInstance(fakeRemoteDataSource,fakeLocaldataSource)
    }


    @Test
    fun getAllFavTest_fackLocalListsize() = runTest{
        val result = repository.getAllFav().first()
        assertThat(result.size,`is`(4))
    }

    @Test
    fun addFavLocation_locationItem_locationItemId() = runTest{
        val result = repository.addFavLocation(addedLocation)
        assertThat(result, `is`(addedLocation.id.toLong()))

    }
}