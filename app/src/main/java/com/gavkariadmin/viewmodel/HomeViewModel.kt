package com.gavkariadmin.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gavkariadmin.Model.ApiResponse
import com.gavkariadmin.Model.SaleAdResponse
import com.gavkariadmin.Model.EventResponse
import com.gavkariadmin.Model.NewsResponse
import com.gavkariadmin.repository.HomeRepository


class HomeViewModel : ViewModel() {

    private val homeRepository = HomeRepository()

    fun getAllVillageEvents(): LiveData<ApiResponse<EventResponse, String>> {
        return homeRepository.accessAllVillageEvents()
    }

    fun getAllNews(): LiveData<ApiResponse<NewsResponse, String>> {
        return homeRepository.accessAllNews()
    }

    fun getAllVillageAds(): LiveData<ApiResponse<SaleAdResponse, String>> {
        return homeRepository.accessAllVillageAd()
    }


}
