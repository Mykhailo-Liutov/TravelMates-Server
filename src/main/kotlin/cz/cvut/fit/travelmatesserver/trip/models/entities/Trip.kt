package cz.cvut.fit.travelmatesserver.trip.models.entities

import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRequirement
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequest
import cz.cvut.fit.travelmatesserver.user.models.User
import java.time.LocalDate
import javax.persistence.*

/**
 * Database entity for Trip object
 */
@Entity(name = "trip")
class Trip(
    @GeneratedValue
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "title")
    val title: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "latitude")
    val latitude: Double,
    @Column(name = "longitude")
    val longitude: Double,
    @Column(name = "suggested_date")
    val suggestedDate: LocalDate,
    @Column(name = "ownerContact")
    val ownerContact: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    val state: TripState,
    @ManyToOne
    @JoinColumn(name = "owner")
    val owner: User,
    @OneToMany(mappedBy = "memberTrip")
    val members: List<TripMember>,
    @OneToMany(mappedBy = "trip")
    val requirements: List<EquipmentRequirement>,
    @OneToMany(mappedBy = "trip")
    val joinRequests: List<JoinRequest>,
    @ElementCollection
    @CollectionTable(name = "trip_images", joinColumns = [JoinColumn(name = "trip_id")])
    @Column(name = "trip_image")
    val tripImages: List<String>
)