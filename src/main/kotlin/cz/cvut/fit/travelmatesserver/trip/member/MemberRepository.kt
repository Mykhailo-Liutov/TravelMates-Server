package cz.cvut.fit.travelmatesserver.trip.member

import cz.cvut.fit.travelmatesserver.trip.models.entities.TripMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<TripMember, Long>