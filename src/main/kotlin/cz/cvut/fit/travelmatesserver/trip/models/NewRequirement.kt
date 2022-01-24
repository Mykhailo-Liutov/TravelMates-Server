package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty

data class NewRequirement(
    @JsonProperty("name")
    val name: String
)