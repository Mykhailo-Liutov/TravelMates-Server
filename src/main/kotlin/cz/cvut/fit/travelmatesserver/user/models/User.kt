package cz.cvut.fit.travelmatesserver.user.models

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * Database entity for User object
 */
@Entity
class User(
    @Id
    val email: String,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = true)
    val picture: String?
)