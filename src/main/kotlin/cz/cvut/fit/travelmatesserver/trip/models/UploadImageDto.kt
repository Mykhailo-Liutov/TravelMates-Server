package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UploadImageDto(
    @JsonProperty("imageRef") val imageRef: String
)