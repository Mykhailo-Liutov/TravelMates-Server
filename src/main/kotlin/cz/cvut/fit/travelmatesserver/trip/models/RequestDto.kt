package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fit.travelmatesserver.user.models.UserDto

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
    val message: String
)