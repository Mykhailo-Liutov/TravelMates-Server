package cz.cvut.fit.travelmatesserver.trip.join

import com.fasterxml.jackson.annotation.JsonProperty

enum class JoinRequestState {
    @JsonProperty("PENDING")
    PENDING,

    @JsonProperty("REJECTED_ALLOW_RESEND")
    REJECTED_ALLOW_RESEND,

    @JsonProperty("REJECTED_NO_RESEND")
    REJECTED_NO_RESEND
}