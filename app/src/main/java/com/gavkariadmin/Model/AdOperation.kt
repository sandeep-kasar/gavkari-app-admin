package com.gavkariadmin.Model

import java.io.Serializable


data class CreateAdBody(
        val user_id: String,
        val village_id: String,
        val village_boy_id: String,
        val type: String,
        val title: String,
        val subtitle: String,
        val family: String,
        val description: String,
        val event_date: String,
        val event_date_ms: String,
        val muhurt: String,
        val event_media: List<EventMedia>,
        val address: String,
        val location: String,
        val latitude: String,
        val longitude: String,
        val contact_no: String,
        val note: String,
        val villages: List<Villages>,
        val amount: String,
        val photo: String,
        var event_id: String,
        var event_aid: String,
        var status: String
) : Serializable

data class EventMedia(
        val id: Int,
        val photo: String
) : Serializable

data class UploadFile(
        val status: String
)


data class EventResponse(
        val status: Int,
        val message: String,
        val AllEvent: ArrayList<AllEvent>
) : Serializable

data class AllEvent(
        val id: String,
        val event_aid: String,
        val user_id: String,
        val village_id: String,
        val status: Int,
        val type: Int,
        val created_at: String,
        val event_date: String,
        val event_date_ms: String,
        val latitude: String,
        val longitude: String,
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
        val photo: String

) : Serializable

data class CommonResponse(
        val status: Int,
        val message: String
)

data class AdVillageResponse(
        val amount: String,
        val message: String,
        val VillageList: Villages,
        val status: Int
)

data class SaveEventResponse(
        val message: String? = null,
        val event: Event? = null,
        val status: Int? = null
)

data class Event(
        val note: String? = null,
        val villageId: String? = null,
        val address: String? = null,
        val eventDateMs: String? = null,
        val latitude: String? = null,
        val createdAt: String? = null,
        val description: String? = null,
        val photo: String? = null,
        val title: String? = null,
        val userId: String? = null,
        val muhurt: String? = null,
        val contactNo: String? = null,
        val eventDate: String? = null,
        val subtitle: String? = null,
        val villageBoyId: String? = null,
        val id: String? = null,
        val family: String? = null,
        val status: String? = null,
        val longitude: String? = null
)

data class SendSmsBody(
        val user_id: String,
        val event_id: String,
        val message: String,
        val mobile_nums: ArrayList<String>
)

data class EventMediaResp(
        val status: Int,
        val message: String,
        val photos: ArrayList<Photos>
)

data class Photos(
        val id: String,
        val event_id: String,
        val type: String,
        val photo: String
)

data class CreateNewsBody(
        val user_id: String,
        val village_id: String,
        val village_boy_id: String,
        val news_type: String,
        var status: String,
        val news_date: String,
        val news_date_ms: String,
        val title: String,
        val photo: String,
        val description: String,
        val news_media: List<EventMedia>
) : Serializable

data class NewsResponse(
        val status: Int,
        val message: String,
        val AllNews: ArrayList<News>
) : Serializable

data class Design(
        val type: Int,
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
        val address: String,
        val photo: String,
        val plan_type:Int
) :Serializable

data class News(
        val id: String,
        val user_id : String,
        val village_id:String,
        val assembly_const_id:String,
        val parliament_const_id:String,
        val news_type:Int,
        val status:Int,
        val news_date:String,
        val news_date_ms:Long,
        val title:String,
        val source:String,
        val photo:String,
        val short_description:String,
        val description:String
) : Serializable