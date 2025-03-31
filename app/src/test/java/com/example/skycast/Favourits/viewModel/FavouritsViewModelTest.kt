package com.example.skycast.Favourits.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.skycast.data.RespondStatus
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.data.repository.Fakerepository
import com.example.skycast.data.repository.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FavouritsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModelTest: FavouritsViewModel
    lateinit var repository: IWeatherRepository
    val locationTest:LocationDataClass=LocationDataClass(id= 78, latitude = "78", longitude = "78", CityName = "arwa")


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun seatUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        repository = Fakerepository()
        viewModelTest = FavouritsViewModel(repository)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }


    @Test
    fun getAllFav_returnsCorrectList() = runTest {
         viewModelTest.getAllFav()
      val list =   viewModelTest.favouritLocationList.first() as RespondStatus.Success<List<LocationDataClass>>

        assertThat(list.result.size ,`is`(2))

        assertThat(list.result[0].id, `is`(1))
        assertThat(list.result[1].id, `is`(2))

    }


    @Test
fun addFavLocation () = runTest {

     viewModelTest.addFavLocation(locationTest)
        advanceUntilIdle()


        viewModelTest.getAllFav()
        val list =   viewModelTest.favouritLocationList.first() as RespondStatus.Success<List<LocationDataClass>>

        assertThat(list.result.size ,`is`(3))
        assertThat(list.result[2].id, `is`(locationTest.id))



    }

}