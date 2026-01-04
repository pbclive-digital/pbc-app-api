package com.kavi.pbc.live.data.model.event

import com.kavi.pbc.live.data.model.BaseModel
import com.kavi.pbc.live.data.model.event.potluck.ContributionItem
import com.kavi.pbc.live.data.model.event.signup.sheet.SignUpSheet
import com.kavi.pbc.live.data.util.DataUtil

data class Event(
    val id: String,
    val name: String,
    val description: String,
    var eventStatus: EventStatus = EventStatus.DRAFT,
    val eventDate: Long,
    val startTime: String,
    val endTime: String,
    val createdTime: Long = System.currentTimeMillis(),
    val venueType: VenueType = VenueType.DRAFT,
    val venue: String?,
    val venueAddress: String?,
    val meetingUrl: String?,
    val creator: String,
    val eventImage: String? = null,
    val eventType: EventType = EventType.DRAFT,
    val registrationRequired: Boolean = false,
    val openSeatCount: Int? = null,
    val potluckAvailable: Boolean = false,
    val potluckItemList: MutableList<ContributionItem>? = mutableListOf(),
    val signUpSheetAvailable: Boolean = false,
    val signUpSheetList: MutableList<SignUpSheet>? = mutableListOf()
): BaseModel {

    constructor(): this (DataUtil.idGenerator("evt"), "", "", EventStatus.DRAFT, 0, "", "",
        System.currentTimeMillis(), VenueType.DRAFT, null, null, null,"", null, EventType.DRAFT, false,
        0, false, mutableListOf(), false, mutableListOf()
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
        "venueAddress" to venueAddress,
        "meetingUrl" to meetingUrl,
        "creator" to creator,
        "eventImage" to eventImage,
        "eventType" to eventType,
        "registrationRequired" to registrationRequired,
        "openSeatCount" to openSeatCount,
        "potluckAvailable" to potluckAvailable,
        "potluckItemList" to potluckItemList,
        "signUpSheetAvailable" to signUpSheetAvailable,
        "signUpSheetList" to signUpSheetList
    )
}
