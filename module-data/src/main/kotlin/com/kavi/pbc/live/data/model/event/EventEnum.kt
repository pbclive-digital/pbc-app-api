package com.kavi.pbc.live.data.model.event

enum class VenueType {
    DEFAULT, PHYSICAL, VIRTUAL
}

enum class EventType {
    DEFAULT, MEDITATION, DHAMMA_TALK, BUDDHISM_CLASS, SPECIAL, RECURRING
}

enum class EventRecurringDay {
    NONE, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

enum class EventStatus {
    DRAFT, PUBLISHED, PASSED
}