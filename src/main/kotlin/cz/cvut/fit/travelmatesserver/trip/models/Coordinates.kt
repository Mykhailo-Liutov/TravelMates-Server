package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * DTO for coordinates
 */
data class Coordinates(
    @JsonProperty("lat")
    val lat: Double,
    @JsonProperty("lon")
    val lon: Double
)