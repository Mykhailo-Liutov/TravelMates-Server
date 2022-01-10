package cz.cvut.fit.travelmatesserver.user.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDto(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("picture")
    val picture: String?
)