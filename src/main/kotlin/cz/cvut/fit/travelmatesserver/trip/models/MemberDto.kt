package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * DTO for member object
 */
data class MemberDto(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("picture")
    val picture: String?,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("contact")
    val contact: String,
    @JsonProperty("providedEquipment")
    val providedEquipment: List<RequirementDto>
)