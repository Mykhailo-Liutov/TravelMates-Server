package cz.cvut.fit.travelmatesserver.trip.models

import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRequirement
import cz.cvut.fit.travelmatesserver.user.models.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "trip_member")
class TripMember(
    @GeneratedValue
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "joined_at")
    val joinedAt: LocalDateTime,
    @OneToMany
    @Column(name = "provided_equipment")
    val providedEquipment: List<EquipmentRequirement>,
    @ManyToOne
    @JoinColumn(name = "trip")
    val trip: Trip,
    @ManyToOne
    @JoinColumn(name = "member_user")
    val memberUser: User
)