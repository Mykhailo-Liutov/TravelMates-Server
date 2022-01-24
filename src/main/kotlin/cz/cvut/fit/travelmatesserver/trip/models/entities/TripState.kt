package cz.cvut.fit.travelmatesserver.trip.models.entities

import com.fasterxml.jackson.annotation.JsonProperty

enum class TripState {
    @JsonProperty("GATHERING")
    GATHERING,

    @JsonProperty("GATHERED")
    GATHERED,

    @JsonProperty("FINISHED")
    FINISHED
}