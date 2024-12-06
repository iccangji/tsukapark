package com.icang.tsukapark.data.model

data class ParkResponse(
	val data: ParkData,
	val topic: String
)

data class ParkData(
	val park2: Boolean,
	val park3: Boolean,
	val park4: Boolean,
	val park5: Boolean,
	val park6: Boolean,
	val park1: Boolean
)

data class OrderResponse(
	val topic: String,
	val data: OrderData,
)
data class OrderData(
	val park2: String,
	val park3: String,
	val park4: String,
	val park5: String,
	val park6: String,
	val park1: String
)
