package com.gavkariadmin.Model

import java.io.Serializable

data class MyAdEdit(
		val id: String,
		val user_id: String,
		val village_id: String,
		val type: String,
		val village_boy_id: String,
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
		val note: String,
		val description: String,
		val photo: String,
		val event_media: List<EventMedia>
)

data class ResponseEventMatter (
	val status: Int,
	val message: String,
	val Matter: List<Matter>
)

data class Matter (
	val id: String,
	val type: String,
	val title: String,
	val subtitle: String,
	val family: String,
	val muhurt: String,
	val place: String,
	val description: String,
	val mobile: String,
	val note: String
) : Serializable
