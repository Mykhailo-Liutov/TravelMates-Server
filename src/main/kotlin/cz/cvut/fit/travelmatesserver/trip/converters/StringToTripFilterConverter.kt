package cz.cvut.fit.travelmatesserver.trip.converters

import cz.cvut.fit.travelmatesserver.trip.TripsFilter
import org.springframework.core.convert.converter.Converter

/**
 * Converts between TripsFilter and String
 */
class StringToTripFilterConverter : Converter<String, TripsFilter> {
    /**
     * Convert given String into TripsFilter
     */
    override fun convert(source: String): TripsFilter {
        return when (source) {
            KEY_EXPLORE -> TripsFilter.EXPLORE
            KEY_MY_TRIPS -> TripsFilter.MY_TRIPS
            else -> TripsFilter.UNKNOWN
        }
    }

    companion object {
        private const val KEY_EXPLORE = "explore"
        private const val KEY_MY_TRIPS = "mytrips"
    }
}

