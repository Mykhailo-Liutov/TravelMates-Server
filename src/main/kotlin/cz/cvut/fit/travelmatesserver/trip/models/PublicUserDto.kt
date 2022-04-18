package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * DTO for a user, which excludes user's email
 */
data class PublicUserDto(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("picture")
    val picture: String?
)