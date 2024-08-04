package com.gavkariadmin.Model

import java.io.Serializable


data class MyVillageEvent(
        val id: String,
        val user_id: String,
        val village_id: String,
        val status: String,
        val plan_type: Int,
        val village_boy_id: String,
        val type: Int,
        val created_at: String,
        val event_date: String,
        val event_date_ms: String,
        val latitude: String="0.0",
        val longitude: String="0.0",
        val address: String,
        val location: String,
        val contact_no: String,
        val title: String,
        val family: String,
        val muhurt: String,
        val subtitle: String,
        val subtitle_one: String,
        val subtitle_two: String,
        val subtitle_three: String,
        val subtitle_four: String,
        val subtitle_five: String,
        val note: String,
        val description: String,
        val description_one: String,
        val photo: String,
        val english: String,
        val marathi: String
) : Serializable

class MyVillageNews(
        val id: String,
        val village_id: String,
        val village_boy_id: String,
        val news_type: String,
        val created_at: String,
        val news_date: String,
        val news_date_ms: String,
        val status: String,
        val title: String,
        val description: String,
        val photo: String
) : Serializable

class AssemblyNews(
        val id: String,
        val village_id: String,
        val village_boy_id: String,
        val news_type: String,
        val created_at: String,
        val news_date: String,
        val news_date_ms: String,
        val status: String,
        val title: String,
        val description: String,
        val photo: String
) : Serializable

class Banner(
        val id: String,
        val banner: String,
        val date :String,
        val type: String
) : Serializable


data class NearByVillageResponse(
        val status: Int,
        val message: String,
        val Banner: List<Banner>,
        val AllVillageEvent: List<AllVillageEvent>,
        val AllVillageNews: List<AllVillageNews>
)

data class AllVillageEvent(
        val id: String,
        val user_id: String,
        val village_id: String,
        val status: String,
        val village_boy_id: String,
        val created_at: String,
        val event_date: String,
        val event_date_ms: String,
        val latitude: String,
        val longitude: String,
        val contact_no: String,
        val address: String,
        val title: String,
        val family: String,
        val muhurt: String,
        val subtitle: String,
        val note: String,
        val description: String,
        val photo: String,
        val english: String,
        val hindi: String,
        val marathi: String
) : Serializable

data class AllVillageNews(
        val id: String,
        val village_id: String,
        val village_boy_id: String,
        val news_type: String,
        val created_at: String,
        val news_date: String,
        val news_date_ms: String,
        val title: String,
        val description: String,
        val photo: String,
        val english: String,
        val hindi: String,
        val marathi: String
) : Serializable


data class MyConnectionResponse(
        val status: Int,
        val message: String,
        val MyConnection: ArrayList<MyConnection>
): Serializable

data class MyConnection(
     val connection_id: String,
     val user_id: String,
     val village_id: String,
     val state_id: String,
     val district_id: String,
     val taluka_id: String,
     val english: String,
     val hindi: String,
     val marathi: String,
     val latitude: String,
     val longitude: String
): Serializable

data class RemoveConnectionResponse(
        val status: Int,
        val message: String

)

data class RssFeedResponse(
        val status: Int,
        val message: String,
        val RssFeed: ArrayList<RssFeed>
): Serializable

data class RssFeed(
        val id : Int,
        val dist_id : Int,
        val rss_one : String,
        val rss_two  : String
)

data class AcceptEventBody(
        val event_id : String,
        val village_id :String,
        val status  : Int
)

data class AcceptNewsBody(
        val news_id : String,
        val village_id :String,
        val status  : Int
)

data class AcceptAdBody(
        val ad_id : String,
        val village_id :String,
        val status  : Int
)

