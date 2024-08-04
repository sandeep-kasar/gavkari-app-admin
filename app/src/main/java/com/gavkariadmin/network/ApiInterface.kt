package com.gavkariadmin.network

import com.gavkariadmin.Model.*
import com.gavkariadmin.constant.HttpConstant.*
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST(SIGN_IN)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun signIn(@Body userSignInBody: SignInBody): Call<SignInResponse>


    //-----------------------------------------------------------------------

    @POST(ALL_EVENTS)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun accessAllEvents(): Call<EventResponse>

    @GET("$GET_EVENT_MEDIA{eventId}")
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun getEventMedia(@Path("eventId") eventId: String): Call<EventMediaResp>

    @POST(ACCEPT_EVENTS)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun acceptEvent(@Body acceptBody: AcceptEventBody): Call<CommonResponse>

    @GET("$MY_AD_DELETE{eventId}")
    fun deleteEvent(@Path("eventId") userId: String): Call<CommonResponse>

    //-----------------------------------------------------------------------

    @POST(ALL_NEWS)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun accessAllNews(): Call<NewsResponse>

    @POST("$GET_NEWS_MEDIA{newsId}")
    @Headers(CACHE_CONTROL)
    fun getNewsMedia(@Path("newsId") newsId: String): Call<EventMediaResp>

    @POST(ACCEPT_NEWS)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun acceptNews(@Body acceptBody: AcceptNewsBody): Call<CommonResponse>

    @GET("$DELETE_NEWS{newsId}")
    fun deleteNews(@Path("newsId") newsId: String): Call<CommonResponse>

    //-----------------------------------------------------------------------

    @POST(ALL_ADS)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun accessAllAds(): Call<SaleAdResponse>

    @POST("$GET_ADS_MEDIA")
    fun getBuySaleMedia(@Body buySaleMediaBody: BuySaleMediaBody): Call<BuySaleMedia>

    @POST("$DELETE_AD{adId}")
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun deleteAd(@Path("adId") adId: String): Call<CommonResponse>

    @POST(ACCEPT_AD)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun acceptAds(@Body acceptAdBody: AcceptAdBody): Call<CommonResponse>

}
