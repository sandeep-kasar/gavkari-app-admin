package com.gavkariadmin.Model

import java.io.Serializable

data class BuySaleBody(
        val user_id:String,
        val village_id:String,
        val latitude:String,
        val longitude:String
)

data class BuySaleFavBody(
        val user_id:String,
        val tab_type:Int,
        val latitude:String,
        val longitude:String
)

data class BuySaleTypeBody(
        val user_id:String,
        val type:Int,
        val sort:Int,
        val filter:Int,
        val latitude:String,
        val longitude:String
)

data class BuySaleResponse(
        val status: Int,
        val message: String,
        val BuySaleAds: ArrayList<BuySale>
)

data class BuySale (
        var id:String,
        var user_id:String,
        var village_id:String,
        var status:Int,
        var tab_type:Int,
        var type:Int,
        var name:String,
        var price:String,
        var breed:String,
        var pregnancies_count:Int,
        var pregnancy_status:Int,
        var milk:Int,
        var weight:String,
        var company:String,
        var model:String,
        var year:String,
        var km_driven:String,
        var power:String,
        var capacity:String,
        var material:String,
        var tynes_count:String,
        var size:String,
        var phase:String,
        var latitude:String,
        var longitude:String,
        var village_en:String,
        var village_mr:String,
        var created_at:String,
        var photo:String,
        var title:String,
        var description:String,
        var fav_user_id:String,
        var distance:Double,
        var fromActivity:String
):Serializable

data class SaleAdResponse(
        val status: Int,
        val message: String,
        val SaleAds: ArrayList<SaleAd>
)

data class SaleAd (
        var id:String,
        var user_id:String,
        var village_id:String,
        var status:Int,
        var tab_type:Int,
        var type:Int,
        var name:String,
        var price:String,
        var breed:String,
        var pregnancies_count:Int,
        var pregnancy_status:Int,
        var milk:Int,
        var weight:String,
        var company:String,
        var model:String,
        var year:String,
        var km_driven:String,
        var power:String,
        var capacity:String,
        var material:String,
        var tynes_count:String,
        var size:String,
        var phase:String,
        var latitude:String,
        var longitude:String,
        var village_en:String,
        var village_mr:String,
        var miliseconds:String,
        var created_at:String,
        var photo:String,
        var title:String,
        var description:String,
        var fav_user_id:String

):Serializable

data class Media(
        val id: Int,
        val photo: String
) : Serializable

data class BuySaleMediaBody(
        val id: String,
        val user_id: String,
        val fav_user_id:String
)

data class BuySaleMedia(
        val status: Int,
        val message: String,
        val photos: ArrayList<Photos>,
        val user: UserShort
)

data class UserShort(
        val name: String,
        val mobile: String
)


