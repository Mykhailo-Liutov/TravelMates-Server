package cz.cvut.fit.travelmatesserver.trip.equipment

import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import javax.persistence.*

/**
 * Database entity for EquipmentRequirement object
 */
@Entity(name = "equipment_requirement")
class EquipmentRequirement(
    @GeneratedValue
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "name")
    val name: String,
    @ManyToOne
    @JoinColumn(name = "trip")
    val trip: Trip
)