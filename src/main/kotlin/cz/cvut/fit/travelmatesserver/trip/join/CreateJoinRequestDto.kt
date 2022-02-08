package cz.cvut.fit.travelmatesserver.trip.join

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateJoinRequestDto(
    @JsonProperty("message") val message: String,
    @JsonProperty("contact") val contact: String,
    @JsonProperty("providedEquipmentIds") val providedEquipmentIds: List<Long>
)
