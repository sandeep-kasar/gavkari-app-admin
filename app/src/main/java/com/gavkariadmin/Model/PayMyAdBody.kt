package com.gavkariadmin.Model

data class PayMyAdBody(
	val amount: String,
	val event_id: String,
	val user_id: String,
	val village_boy_id: String,
	val transaction_no: String,
	val payment_date: String,
	val status: String
)

