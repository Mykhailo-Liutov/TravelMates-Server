package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestState


/**
 * DTO for join request object
 */
data class RequestDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("user")
    val user: PublicUserDto,
    @JsonProperty("providedEquipment")
    val providedEquipment: List<RequirementDto>,
    @JsonProperty("contact")
    val contact: String,
    @JsonProperty("message")
    val message: String,
    @JsonProperty("state")
    val state: JoinRequestState,
    @JsonProperty("rejectionReason")
    val rejectionReason: String?
)