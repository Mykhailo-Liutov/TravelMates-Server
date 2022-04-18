package cz.cvut.fit.travelmatesserver.trip.models.entities

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents state of a trip
 */
enum class TripState {
    /**
     * A trip which is actively gathering members
     */
    @JsonProperty("GATHERING")
    GATHERING,

    /**
     * A trip which has stopped gathering people
     */
    @JsonProperty("GATHERED")
    GATHERED,

    /**
     * A trip which already happened
     */
    @JsonProperty("FINISHED")
    FINISHED
}