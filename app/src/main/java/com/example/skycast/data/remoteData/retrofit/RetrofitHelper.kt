package com.example.skycast.data.remoteData.retrofit
import com.example.skycast.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private var retrofit: Retrofit? =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val apiServices = retrofit?.create(ApiServices::class.java)

}
