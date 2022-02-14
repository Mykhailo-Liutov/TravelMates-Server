package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty

enum class UserType {
    @JsonProperty("GUEST")
    GUEST,

    @JsonProperty("MEMBER")
    MEMBER,

    @JsonProperty("OWNER")
    OWNER
}