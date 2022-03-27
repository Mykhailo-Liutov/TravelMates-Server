package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TripsRepository : JpaRepository<Trip, Long> {

    //TODO Add trips with join requests
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

    @Query(
        nativeQuery = true,
        value = "SELECT trip.*\n" +
                "FROM trip\n" +
                "         LEFT JOIN trip_member ON trip_member.member_trip_id = trip.id\n" +
                "WHERE trip.owner != :userEmail AND (\n" +
                "      trip_member.member_user IS NULL OR trip_member.member_user != :userEmail)"
    )
    fun findExploreTrips(@Param("userEmail") userEmail: String): List<Trip>

    fun findTripById(tripId: Long): Trip
}