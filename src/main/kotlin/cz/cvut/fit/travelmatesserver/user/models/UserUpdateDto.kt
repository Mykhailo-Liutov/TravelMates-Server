package cz.cvut.fit.travelmatesserver.user.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UserUpdateDto(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("picture")
    val picture: String?
)