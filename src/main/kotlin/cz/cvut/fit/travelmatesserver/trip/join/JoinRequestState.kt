package cz.cvut.fit.travelmatesserver.trip.join

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents states of join requests
 */
enum class JoinRequestState {
    /**
     * Pending request which trip owner hasn't reviewed yet
     */
    @JsonProperty("PENDING")
    PENDING,

    /**
     * Rejected request, with possibility of sender to send new requests
     */
    @JsonProperty("REJECTED_ALLOW_RESEND")
    REJECTED_ALLOW_RESEND,

    /**
     * Rejected request, with no possibility of sender to send new requests
     */
    @JsonProperty("REJECTED_NO_RESEND")
    REJECTED_NO_RESEND
}