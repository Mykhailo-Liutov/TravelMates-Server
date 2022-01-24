package cz.cvut.fit.travelmatesserver.trip.equipment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EquipmentRepository : JpaRepository<EquipmentRequirement, Long>