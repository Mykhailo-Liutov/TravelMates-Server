package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * DTO for uploading a trip image
 */
data class UploadImageDto(
    @JsonProperty("imageRef") val imageRef: String
)