package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * DTO for creating new equipment requirement
 */
data class NewRequirement(
    @JsonProperty("name")
    val name: String
)