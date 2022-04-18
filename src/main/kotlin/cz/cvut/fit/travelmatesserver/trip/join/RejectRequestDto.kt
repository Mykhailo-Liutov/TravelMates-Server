package cz.cvut.fit.travelmatesserver.trip.join

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * DTO for rejecting a join request
 */
data class RejectRequestDto(
    @JsonProperty("reason") val reason: String,
    @JsonProperty("allowResend") val allowResend: Boolean
)