package cz.cvut.fit.travelmatesserver.trip

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty

enum class TripsFilter {
    EXPLORE,
    MY_TRIPS,
    UNKNOWN
}