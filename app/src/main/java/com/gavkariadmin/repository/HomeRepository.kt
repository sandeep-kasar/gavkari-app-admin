package com.gavkariadmin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.Model.SaleAdResponse
import com.gavkariadmin.Model.EventResponse
import com.gavkariadmin.Model.NewsResponse
import com.gavkariadmin.network.ApiClient
import com.gavkariadmin.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeRepository {


    fun accessAllVillageEvents(): LiveData<ApiResponse<EventResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<EventResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessAllEvents()
                .enqueue(object : Callback<EventResponse> {
                    override fun onResponse(call: Call<EventResponse>?, response: Response<EventResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response!!.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<EventResponse>?, t: Throwable?) {

                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    fun accessAllNews(): LiveData<ApiResponse<NewsResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<NewsResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
            .accessAllNews()
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>?, response: Response<NewsResponse>?) {
                    if (response!!.code() == 200) {
                        responseData.postValue(ApiResponse(response!!.body(), null))
                    }
                }

                override fun onFailure(call: Call<NewsResponse>?, t: Throwable?) {

                    responseData.postValue(ApiResponse(null, t!!.message))
                }
            })

        return responseData
    }

    fun accessAllVillageAd(): LiveData<ApiResponse<SaleAdResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<SaleAdResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
            .accessAllAds()
            .enqueue(object : Callback<SaleAdResponse> {
                override fun onResponse(call: Call<SaleAdResponse>?, response: Response<SaleAdResponse>?) {
                    if (response!!.code() == 200) {
                        responseData.postValue(ApiResponse(response!!.body(), null))
                    }
                }

                override fun onFailure(call: Call<SaleAdResponse>?, t: Throwable?) {

                    responseData.postValue(ApiResponse(null, t!!.message))
                }
            })

        return responseData
    }


}


