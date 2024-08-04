package com.gavkariadmin.Model

import java.io.Serializable


data class VillageListBody(
        val latitude: String,
        val longitude: String
)


data class VillageListResponse(
        val status: Int,
        val message: String,
        val VillageList: ArrayList<Villages>
)

data class Villages(
        val id: String,
        val state_id: String,
        val district_id: String,
        val taluka_id: String,
        val english: String,
        val hindi: String,
        val marathi: String,
        val latitude: String,
        val longitude: String,
        val distance: String
) : Serializable



data class AddVillageBody(
        val user_id: String,
        val village_id: String
)

data class RemoveAdVillage(
        val event_id: String,
        val village_id: String
)


data class AddEventVillageBody(
        val event_id: String,
        val village_id: String
)


data class AddVillageResponse(
        val status: Int,
        val message: String,
        val connectionId: String,
        val connection: String,
        val permission: String
) : Serializable

data class AddEventVillageResponse(
        val amount: String? = null,
        val message: String? = null,
        val status: Int? = null
)

data class EventVillage(
        val village_id: String,
        val state_id: String,
        val district_id: String,
        val taluka_id: String,
        val english: String,
        val hindi: String,
        val marathi: String,
        val latitude: String,
        val longitude: String,
        val amount: String
): Serializable

