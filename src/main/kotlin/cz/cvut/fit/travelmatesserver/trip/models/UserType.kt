package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Type of user in a given trip
 */
enum class UserType {
    /**
     * Represents a user who is not a member or owner of a trip
     */
    @JsonProperty("GUEST")
    GUEST,

    /**
     * Represents a user who is a member of the trip
     */
    @JsonProperty("MEMBER")
    MEMBER,

    /**
     * Represents a user who is the owner of the trip
     */
    @JsonProperty("OWNER")
    OWNER
}