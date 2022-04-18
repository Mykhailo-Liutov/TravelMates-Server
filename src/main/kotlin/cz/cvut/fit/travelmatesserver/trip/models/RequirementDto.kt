package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * DTO for trip requirement object
 */
data class RequirementDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("isFulfilled")
    val isFulfilled: Boolean
)