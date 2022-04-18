package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TripsRepository : JpaRepository<Trip, Long> {

    /**
     * @param userEmail email of user, based on which filtering is done
     * @return List of my trips of current user, which are trips where user is owner,
     * member, or has sent a join request
     */
    @Query(
        nativeQuery = true,
        value = "SELECT trip.*\n" +
                "FROM trip\n" +
                "         LEFT JOIN trip_member ON trip_member.member_trip_id = trip.id\n" +
                "         LEFT JOIN join_request ON join_request.trip = trip.id\n" +
                "WHERE trip.owner = :userEmail OR\n" +
                "      trip_member.member_user = :userEmail OR\n" +
                "      join_request.sender = :userEmail"
    )
    fun findMyTrips(@Param("userEmail") userEmail: String): List<Trip>

    /**
     * @param userEmail email of user, based on which filtering is done
     * @return List of explore trips of current user, which are trips where user is not an owner,
     * a member, and hasn't sent a join request
     */
    @Query(
        nativeQuery = true,
        value = "SELECT trip.*\n" +
                "FROM trip\n" +
                "         LEFT JOIN trip_member ON trip_member.member_trip_id = trip.id\n" +
                "         LEFT JOIN join_request ON join_request.trip = trip.id\n" +
                "WHERE trip.owner != :userEmail AND (\n" +
                "      trip_member.member_user IS NULL OR trip_member.member_user != :userEmail) AND" +
                "      (join_request.sender IS NULL OR join_request.sender != :userEmail) AND" +
                "       (trip.state = \"GATHERING\")"
    )
    fun findExploreTrips(@Param("userEmail") userEmail: String): List<Trip>

    /**
     * Finds a trip with given id, or throws an Exception
     * @param tripId id of a trip to find
     * @return the found trip
     */
    fun findTripById(tripId: Long): Trip
}