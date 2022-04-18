package cz.cvut.fit.travelmatesserver.trip

/**
 * Represents filters for getting a list of trips
 */
enum class TripsFilter {
    /**
     * Trips which user hasn't interacted with yet
     */
    EXPLORE,

    /**
     * Trips which user has interacted with
     */
    MY_TRIPS,

    /**
     * Fallback value
     */
    UNKNOWN
}