package cz.cvut.fit.travelmatesserver.trip.join

import cz.cvut.fit.travelmatesserver.trip.models.Trip
import cz.cvut.fit.travelmatesserver.user.models.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "join_request")
class JoinRequest(
    @GeneratedValue
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "sent_at")
    val sentAt: LocalDateTime,
    @Column(name = "message")
    val message: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    val state: JoinRequestState,
    @Column(name = "contact")
    val contact: String,
    @ManyToOne
    @JoinColumn(name = "sender")
    val sender: User,
    @ManyToOne
    @JoinColumn(name = "trip")
    val trip: Trip
)