package com.kavi.pbc.live.data.model.event

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.util.DataUtil

data class Event(
    val id: String,
    val name: String,
    val description: String,
    var eventStatus: EventStatus = EventStatus.DRAFT,
    val eventDate: Long,
    val startTime: String,
    val endTime: String,
    val createdTime: Long,
    val venueType: VenueType = VenueType.VIRTUAL,
    val venue: String?,
    val creator: String,
    val eventImage: String? = null,
    val eventType: EventType = EventType.SPECIAL,
    val isRegistrationRequired: Boolean = false,
    val openSeatCount: Int? = null,
    val isPotluckAvailable: Boolean = false,
    val potluckItemList: MutableList<PotluckItem>? = mutableListOf()
): BaseModel {

    constructor(): this (DataUtil.idGenerator("evt"), "", "", EventStatus.DRAFT, 0, "", "",
        System.currentTimeMillis(), VenueType.VIRTUAL, null, "", null, EventType.SPECIAL, false,
        0, false, mutableListOf()
    )

    override fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "description" to description,
        "eventStatus" to eventStatus,
        "eventDate" to eventDate,
        "startTime" to startTime,
        "endTime" to endTime,
        "createdTime" to createdTime,
        "venueType" to venueType,
        "venue" to venue,
        "creator" to creator,
        "eventImage" to eventImage,
        "eventType" to eventType,
        "isRegistrationRequired" to isRegistrationRequired,
        "openSeatCount" to openSeatCount,
        "isPotluckAvailable" to isPotluckAvailable,
        "potluckItemList" to potluckItemList
    )
}
