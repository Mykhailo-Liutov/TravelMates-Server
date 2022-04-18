package cz.cvut.fit.travelmatesserver.trip.join

import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface JoinRequestRepository : JpaRepository<JoinRequest, Long> {
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM join_request WHERE sender=:userEmail AND trip=:tripId"
    )
    fun findRequest(@Param("userEmail") userEmail: String, @Param("tripId") tripId: Long): JoinRequest?
}